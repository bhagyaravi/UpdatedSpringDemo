package jp.co.alico.cusext.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.co.alico.cusext.common.LoggerUtil;
import jp.co.alico.cusext.common.Constant;
import jp.co.alico.cusext.form.DisAgrEntryForm;
import jp.co.alico.cusext.vo.DisAgrmntInfoVO;

/**
 * <p>システム名： CUSEXT</p>
 * <p>業務名： 不成約情報操作クラス</p>
 * <p>概要： 不成約情報の取得、登録、修正を行う。</p>
 * <p>作成日： 2010/10/22</p>
 *
 * @version 1.0.0
 */
public class DisAgrInfoDAO extends DAOBase {
    // Logインスタンスの生成
    private LoggerUtil logger = new LoggerUtil(this.getClass().getName());

    /**
     * 不成約情報一覧を取得する。
     * @param actId 不成約情報を取得する活動情報のID
     * @return List<DisAgrmntInfoVO> 不成約情報を持つリスト
     * @throws Exception 例外処理
     * @throws SQLException 例外処理
     */
    public List<DisAgrmntInfoVO> getDisAgrInfoList(String actId) throws Exception, SQLException {
        return getDisAgrInfoList(actId, true);
    }

    /**
     * 不成約情報一覧を取得する。
     * @param actId 不成約情報を取得する活動情報のID
     * @param isBranch true - 支社マLeads、false - AG本部Leads
     * @return List<DisAgrmntInfoVO> 不成約情報を持つリスト
     * @throws Exception 例外処理
     * @throws SQLException 例外処理
     */
    public List<DisAgrmntInfoVO> getDisAgrInfoList(String actId, boolean isBranch) throws Exception, SQLException {
        if (logger.isDebugEnabled()) {
            logger.methodLog("getDisAgrInfoList", new Object[]{actId}, Constant.METHOD_START);
        }

        // DBコネクション用
        Connection conn = null;
        // SQL文実行用
        PreparedStatement pstmt = null;
        // SQL実行結果取得用
        ResultSet rset = null;
        // 成約情報リスト
        List<DisAgrmntInfoVO> disAgrInfoList = new ArrayList<DisAgrmntInfoVO>();
        // 実行SQL文
        String strSql = null;
        // 成約情報を持つオブジェクト
        DisAgrmntInfoVO disAgrInfoVO = null;

        try {
            conn = getConnection();

            strSql = getDisAgrInfoSql(false, isBranch);

            // 実行するSQL文をログに出力
            logger.printSql(strSql);

            // SQL文設定
            pstmt = conn.prepareStatement(strSql);

            pstmt.setString(1, actId);

            // SQL文実行
            rset = pstmt.executeQuery();

            // 該当データがあった場合はリストに設定
            while (rset.next()) {
                disAgrInfoVO = new DisAgrmntInfoVO();
                disAgrInfoVO.setDisAgrmntNo(rset.getString("AGRMNT_NO"));                           // 不成約No
                disAgrInfoVO.setCompeAterCmpny1(rset.getString("COMPE_ATER_CMPNY1"));               // 競合他社1
                disAgrInfoVO.setCompeAterCmpny1Prdct(rset.getString("COMPE_ATER_CMPNY1_PRDCT"));    // 競合他社1商品
                disAgrInfoVO.setMemo(rset.getString("MEMO"));                                       // その他メモ
                disAgrInfoVO.setEntryDt(rset.getString("ENTRY_DT"));                                // 登録日時
                disAgrInfoVO.setUpdateDt(rset.getString("UPDATE_DT"));                              // 更新日時
                disAgrInfoList.add(disAgrInfoVO);
            }

        } catch (SQLException sqle) {
            logger.error("E0002", sqle);
            throw sqle;
        } catch (Exception e) {
            logger.error("E0003", e);
            throw e;
        } finally {
            try {
                close(conn, pstmt, rset);
            } finally {
                rset = null;
                pstmt = null;
                conn = null;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.methodLog("getDisAgrInfoList", new Object[]{disAgrInfoList}, Constant.METHOD_END);
        }
        return disAgrInfoList;
    }

    /**
     * 不成約情報の詳細を取得する。
     * @param disAgrmntNo 不成約No
     * @return DisAgrmntInfoVO 不成約詳細情報を持つオブジェクト
     * @throws Exception 例外処理
     * @throws SQLException 例外処理
     */
    public DisAgrmntInfoVO getDisAgrDetailInfo(String disAgrmntNo) throws Exception, SQLException {
        return getDisAgrDetailInfo(disAgrmntNo, true);
    }

    /**
     * 不成約情報の詳細を取得する。
     * @param disAgrmntNo 不成約No
     * @param isBranch true - 支社マLeads、false - AG本部Leads
     * @return DisAgrmntInfoVO 不成約詳細情報を持つオブジェクト
     * @throws Exception 例外処理
     * @throws SQLException 例外処理
     */
    public DisAgrmntInfoVO getDisAgrDetailInfo(String disAgrmntNo, boolean isBranch) throws Exception, SQLException {
        if (logger.isDebugEnabled()) {
            logger.methodLog("getDisAgrDetailInfo", Constant.METHOD_START);
        }

        // DBコネクション用
        Connection conn = null;
        // SQL文実行用
        PreparedStatement pstmt = null;
        // SQL実行結果取得用
        ResultSet rset = null;
        // 実行SQL文
        String strSql = null;
        // 不成約情報を持つオブジェクト
        DisAgrmntInfoVO disAgrInfoVO = null;

        try {
            conn = getConnection();

            strSql = getDisAgrInfoSql(true, isBranch);

            // 実行するSQL文をログに出力
            logger.printSql(strSql);

            // SQL文設定
            pstmt = conn.prepareStatement(strSql);
            pstmt.setString(1, disAgrmntNo);

            // SQL文実行
            rset = pstmt.executeQuery();

            // 該当データがあった場合はリストに設定
            if (rset.next()) {
                disAgrInfoVO = new DisAgrmntInfoVO();
                disAgrInfoVO.setDisAgrmntNo(rset.getString("AGRMNT_NO"));                           // 不成約No
                disAgrInfoVO.setActSubject(rset.getString("ACT_SUBJECT"));                          // 活動件名
                disAgrInfoVO.setHouseholdId(rset.getString("HOUSEHOLD_ID"));                        // 関連顧客ＩＤ
                disAgrInfoVO.setTelNg(rset.getString("TEL_NG"));                                    // 連絡が取れない・面談ができない
                disAgrInfoVO.setFrmAcquaintanceApps(rset.getString("FRM_ACQUAINTANCE_APPS"));       // 他募集人もしくは通販より契約
                disAgrInfoVO.setUndertakeNg(rset.getString("UNDERTAKE_NG"));                        // 引受が不可能である
                disAgrInfoVO.setCnfdncNg(rset.getString("CNFDNC_NG"));                              // 信用がない
                disAgrInfoVO.setMemo(rset.getString("MEMO"));                                       // その他メモ
                disAgrInfoVO.setCompeAterCmpny1(rset.getString("COMPE_ATER_CMPNY1"));               // 競合他社1
                disAgrInfoVO.setCompeAterCmpny1Prdct(rset.getString("COMPE_ATER_CMPNY1_PRDCT"));    // 競合他社1商品
                disAgrInfoVO.setCompeAterCmpny2(rset.getString("COMPE_ATER_CMPNY2"));               // 競合他社2
                disAgrInfoVO.setCompeAterCmpny2Prdct(rset.getString("COMPE_ATER_CMPNY2_PRDCT"));    // 競合他社2商品
                disAgrInfoVO.setCompeAterCmpny3(rset.getString("COMPE_ATER_CMPNY3"));               // 競合他社3
                disAgrInfoVO.setCompeAterCmpny3Prdct(rset.getString("COMPE_ATER_CMPNY3_PRDCT"));    // 競合他社3商品
                disAgrInfoVO.setEntryPgId(rset.getString("ENTRY_PG_ID"));                           // 登録プログラムID
                disAgrInfoVO.setEntryDt(rset.getString("ENTRY_DT"));                                // 登録日時
                disAgrInfoVO.setEntryBy(rset.getString("ENTRY_BY"));                                // 登録者ID
                disAgrInfoVO.setUpdatePgId(rset.getString("UPDATE_PG_ID"));                         // 更新プログラムID
                disAgrInfoVO.setUpdateDt(rset.getString("UPDATE_DT"));                              // 更新日時
                disAgrInfoVO.setUpdateBy(rset.getString("UPDATE_BY"));                              // 更新者ID
            }

        } catch (SQLException sqle) {
            logger.error("E0002", sqle);
            throw sqle;
        } catch (Exception e) {
            logger.error("E0003", e);
            throw e;
        } finally {
            try {
                close(conn, pstmt, rset);
            } finally {
                rset = null;
                pstmt = null;
                conn = null;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.methodLog("getDisAgrDetailInfo", new Object[]{disAgrInfoVO}, Constant.METHOD_END);
        }
        return disAgrInfoVO;
    }

    /**
     * 不成約情報を登録する。
     * @param disAgrEntryForm 登録する情報を持つオブジェクト
     * @param opeId 登録者のOpeId
     * @return 登録した不成約情報
     * @throws Exception 例外処理
     * @throws SQLException 例外処理
     */
    public synchronized DisAgrmntInfoVO insertDisAgrInfo(DisAgrEntryForm disAgrEntryForm, String opeId) throws Exception, SQLException {
        if (logger.isDebugEnabled()) {
            logger.methodLog("insertDisAgrInfo", new Object[]{disAgrEntryForm, opeId}, Constant.METHOD_START);
        }

        // DBコネクション用
        Connection conn = null;
        // SQL文実行用
        PreparedStatement pstmt = null;
        // SQL実行結果取得用
        ResultSet rset = null;
        // 実行SQL文
        StringBuffer strSql = new StringBuffer();
        // 登録した不成約No
        String disAgrNo = null;

        try {
            conn = getConnection();

            // キー情報取得
            pstmt = conn.prepareStatement("SELECT 'EN' + CAST(NEXT VALUE FOR " + Constant.SEQ_SCHEMA + ".SEQ_MST_CUS_AGRMNT_NO_EXT AS VARCHAR) AS AGRMNT_NO");
            rset = pstmt.executeQuery();
            if (rset.next()) {
                disAgrNo = rset.getString(1);
            }
            pstmt.close();
            rset.close();

            /** 2016/05/18 王　新 ADA IAライン対応Phase2　1.1 CUSEXTでのデータ登録先変更 ADD Begin */
            //strSql.append("insert into ").append(Constant.TABLE_SCHEMA).append(".").append("T_MST_CUS_LDS_AGRMNT_INFO_EXT (");
            strSql.append("insert into ").append(Constant.TABLE_SCHEMA).append(".").append("T_MST_CUS_LDS_AGRMNT_INFO_DT (");
            /** 2016/05/18 王　新 ADA IAライン対応Phase2　1.1 CUSEXTでのデータ登録先変更 ADD End */
            strSql.append("  SFDC_ID");                                                 // 成約・不成約主キー
            strSql.append("  ,AGRMNT_NO");                                              // 不成約No
            strSql.append("  ,ACT_SFDC_ID");                                            // 活動情報のID
            strSql.append("  ,AGRMNT_NAGRMNT");                                         // 成約/不成約
            strSql.append("  ,ACT_SUBJECT");                                            // 活動件名
            strSql.append("  ,HOUSEHOLD_ID");                                           // 関連顧客ＩＤ
            strSql.append("  ,TEL_NG");                                                 // 連絡が取れない・面談ができない
            strSql.append("  ,CNFDNC_NG");                                              // その他
            strSql.append("  ,FRM_ACQUAINTANCE_APPS");                                  // 他募集人もしくは通販より契約
            strSql.append("  ,UNDERTAKE_NG");                                           // 引受が不可能である
            strSql.append("  ,MEMO");                                                   // その他メモ
            strSql.append("  ,COMPE_ATER_CMPNY1");                                      // 競合他社1
            strSql.append("  ,COMPE_ATER_CMPNY1_PRDCT");                                // 競合他社1商品
            strSql.append("  ,COMPE_ATER_CMPNY2");                                      // 競合他社2
            strSql.append("  ,COMPE_ATER_CMPNY2_PRDCT");                                // 競合他社2商品
            strSql.append("  ,COMPE_ATER_CMPNY3");                                      // 競合他社3
            strSql.append("  ,COMPE_ATER_CMPNY3_PRDCT");                                // 競合他社3商品
            strSql.append("  ,DEL_FLG");                                                // 削除フラグ
            strSql.append("  ,ENTRY_PG_ID");                                            // 登録プログラムID
            strSql.append("  ,ENTRY_DT");                                               // 登録日時
            strSql.append("  ,ENTRY_BY");                                               // 登録者ID
            strSql.append("  ,UPDATE_PG_ID");                                           // 更新プログラムID
            strSql.append("  ,UPDATE_DT");                                              // 更新日時
            strSql.append("  ,UPDATE_BY");                                              // 更新者ID
            strSql.append("  ) values (");
            strSql.append("  'EXT_AGR_' + CAST(NEXT VALUE FOR " + Constant.SEQ_SCHEMA + ".SEQ_MST_CUS_AGRMNT_ID_EXT AS VARCHAR)");
            strSql.append("  ,?");
            strSql.append("  ,?");
            strSql.append("  ,'不成約'");
            strSql.append("  ,?");
            strSql.append("  ,?");
            strSql.append("  ,?");
            strSql.append("  ,?");
            strSql.append("  ,?");
            strSql.append("  ,?");
            strSql.append("  ,?");
            strSql.append("  ,?");
            strSql.append("  ,?");
            strSql.append("  ,?");
            strSql.append("  ,?");
            strSql.append("  ,?");
            strSql.append("  ,?");
            strSql.append("  ,'FALSE'");
            strSql.append("  ,'CUSEXT', GETDATE(), ?");
            strSql.append("  ,'CUSEXT', GETDATE(), ?");
            strSql.append("  )");

            // 実行するSQL文をログに出力
            logger.printSql(strSql.toString());

            // SQL文設定
            pstmt = conn.prepareStatement(strSql.toString());
            pstmt.setString(1, disAgrNo);
            pstmt.setString(2, disAgrEntryForm.getActId());
            pstmt.setString(3, disAgrEntryForm.getActSubject());
            pstmt.setString(4, disAgrEntryForm.getHouseholdId());
            pstmt.setString(5, disAgrEntryForm.getTelNg());
            pstmt.setString(6, disAgrEntryForm.getCnfdncNg());
            pstmt.setString(7, disAgrEntryForm.getFrmAcquaintanceApps());
            pstmt.setString(8, disAgrEntryForm.getUndertakeNg());
            pstmt.setString(9, disAgrEntryForm.getMemo());
            pstmt.setString(10, disAgrEntryForm.getCompeAterCmpny1());
            pstmt.setString(11, disAgrEntryForm.getCompeAterCmpny1Prdct());
            pstmt.setString(12, disAgrEntryForm.getCompeAterCmpny2());
            pstmt.setString(13, disAgrEntryForm.getCompeAterCmpny2Prdct());
            pstmt.setString(14, disAgrEntryForm.getCompeAterCmpny3());
            pstmt.setString(15, disAgrEntryForm.getCompeAterCmpny3Prdct());
            pstmt.setString(16, opeId);
            pstmt.setString(17, opeId);

            // 不成約情報登録
            pstmt.executeUpdate();

        } catch (SQLException sqle) {
            logger.error("E0002", sqle);
            throw sqle;
        } catch (Exception e) {
            logger.error("E0003", e);
            throw e;
        } finally {
            try {
                close(conn, pstmt, null);
            } finally {
                pstmt = null;
                conn = null;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.methodLog("insertDisAgrInfo", Constant.METHOD_END);
        }
        return getDisAgrDetailInfo(disAgrNo);
    }

    /**
     * 不成約情報を更新する。
     * @param disAgrEntryForm 更新する情報を持つオブジェクト
     * @param opeId 更新者のOpeId
     * @return 登録した不成約情報
     * @throws Exception 例外処理
     * @throws SQLException 例外処理
     */
    public synchronized DisAgrmntInfoVO updateDisAgrInfo(DisAgrEntryForm disAgrEntryForm, String opeId) throws Exception, SQLException {
        if (logger.isDebugEnabled()) {
            logger.methodLog("updateDisAgrInfo", new Object[]{disAgrEntryForm, opeId}, Constant.METHOD_START);
        }

        // DBコネクション用
        Connection conn = null;
        // SQL文実行用
        PreparedStatement pstmt = null;
        // 実行SQL文
        StringBuffer strSql = new StringBuffer();

        try {
            conn = getConnection();

            /** 2016/05/18 王　新 ADA IAライン対応Phase2　1.1 CUSEXTでのデータ登録先変更 ADD Begin */
            //strSql.append("update ").append(Constant.TABLE_SCHEMA).append(".").append("T_MST_CUS_LDS_AGRMNT_INFO_EXT set ");
            strSql.append("update ").append(Constant.TABLE_SCHEMA).append(".").append("T_MST_CUS_LDS_AGRMNT_INFO_DT set ");
            /** 2016/05/18 王　新 ADA IAライン対応Phase2　1.1 CUSEXTでのデータ登録先変更 ADD End */
            strSql.append("  ACT_SUBJECT = ?");                                             // 活動件名
            strSql.append("  ,TEL_NG = ?");                                                 // 連絡が取れない・面談ができない
            strSql.append("  ,CNFDNC_NG = ?");                                              // その他
            strSql.append("  ,FRM_ACQUAINTANCE_APPS = ?");                                  // 他募集人もしくは通販より契約
            strSql.append("  ,UNDERTAKE_NG = ?");                                           // 引受が不可能である
            strSql.append("  ,MEMO = ?");                                                   // その他メモ
            strSql.append("  ,COMPE_ATER_CMPNY1 = ?");                                      // 競合他社1
            strSql.append("  ,COMPE_ATER_CMPNY1_PRDCT = ?");                                // 競合他社1商品
            strSql.append("  ,COMPE_ATER_CMPNY2 = ?");                                      // 競合他社2
            strSql.append("  ,COMPE_ATER_CMPNY2_PRDCT = ?");                                // 競合他社2商品
            strSql.append("  ,COMPE_ATER_CMPNY3 = ?");                                      // 競合他社3
            strSql.append("  ,COMPE_ATER_CMPNY3_PRDCT = ?");                                // 競合他社3商品
            strSql.append("  ,UPDATE_PG_ID = 'CUSEXT'");                                    // 更新プログラムID
            strSql.append("  ,UPDATE_DT = GETDATE()");                                        // 更新日時
            strSql.append("  ,UPDATE_BY = ? ");                                             // 更新者ID
            strSql.append(" where ");
            strSql.append("  AGRMNT_NO = ?");         // 不成約No

            // 実行するSQL文をログに出力
            logger.printSql(strSql.toString());

            // SQL文設定
            pstmt = conn.prepareStatement(strSql.toString());
            pstmt.setString(1, disAgrEntryForm.getActSubject());
            pstmt.setString(2, disAgrEntryForm.getTelNg());
            pstmt.setString(3, disAgrEntryForm.getCnfdncNg());
            pstmt.setString(4, disAgrEntryForm.getFrmAcquaintanceApps());
            pstmt.setString(5, disAgrEntryForm.getUndertakeNg());
            pstmt.setString(6, disAgrEntryForm.getMemo());
            pstmt.setString(7, disAgrEntryForm.getCompeAterCmpny1());
            pstmt.setString(8, disAgrEntryForm.getCompeAterCmpny1Prdct());
            pstmt.setString(9, disAgrEntryForm.getCompeAterCmpny2());
            pstmt.setString(10, disAgrEntryForm.getCompeAterCmpny2Prdct());
            pstmt.setString(11, disAgrEntryForm.getCompeAterCmpny3());
            pstmt.setString(12, disAgrEntryForm.getCompeAterCmpny3Prdct());
            pstmt.setString(13, opeId);
            pstmt.setString(14, disAgrEntryForm.getDisAgrmntNo());

            // 不成約情報更新
            pstmt.executeUpdate();

        } catch (SQLException sqle) {
            logger.error("E0002", sqle);
            throw sqle;
        } catch (Exception e) {
            logger.error("E0003", e);
            throw e;
        } finally {
            try {
                close(conn, pstmt, null);
            } finally {
                pstmt = null;
                conn = null;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.methodLog("updateDisAgrInfo", Constant.METHOD_END);
        }
        return getDisAgrDetailInfo(disAgrEntryForm.getDisAgrmntNo());
    }

    /**
     * 不成約情報を削除する。
     * @param disAgrId 削除する不成約情報のID
     * @throws Exception 例外処理
     * @throws SQLException 例外処理
     */
    public void deleteDisAgrInfo(String disAgrId) throws Exception, SQLException {
        if (logger.isDebugEnabled()) {
            logger.methodLog("deleteDisAgrInfo", new Object[]{disAgrId}, Constant.METHOD_START);
        }

        // DBコネクション用
        Connection conn = null;
        // SQL文実行用
        PreparedStatement pstmt = null;
        // 実行SQL文
        StringBuffer strSql = new StringBuffer();

        try {
            conn = getConnection();

            /** 2016/05/18 王　新 ADA IAライン対応Phase2　1.1 CUSEXTでのデータ登録先変更 ADD Begin */
            //strSql.append("delete from ").append(Constant.TABLE_SCHEMA).append(".").append("T_MST_CUS_LDS_AGRMNT_INFO_EXT where AGRMNT_NO = ?");
            strSql.append("delete from ").append(Constant.TABLE_SCHEMA).append(".").append("T_MST_CUS_LDS_AGRMNT_INFO_DT where AGRMNT_NO = ?");
            /** 2016/05/18 王　新 ADA IAライン対応Phase2　1.1 CUSEXTでのデータ登録先変更 ADD End */
            // 実行するSQL文をログに出力
            logger.printSql(strSql.toString());

            // SQL文設定
            pstmt = conn.prepareStatement(strSql.toString());
            pstmt.setString(1, disAgrId);

            // 不成約情報削除
            pstmt.executeUpdate();

        } catch (SQLException sqle) {
            logger.error("E0002", sqle);
            throw sqle;
        } catch (Exception e) {
            logger.error("E0003", e);
            throw e;
        } finally {
            try {
                close(conn, pstmt, null);
            } finally {
                pstmt = null;
                conn = null;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.methodLog("deleteDisAgrInfo", Constant.METHOD_END);
        }
    }

    /**
     * 渡された活動情報IDに関連する不成約情報を削除する。
     * @param actId 削除する不成約情報の活動ID
     * @param conn コネクション
     * @throws Exception 例外処理
     * @throws SQLException 例外処理
     */
    public void deleteAllDisAgrInfo(String actId, Connection conn) throws Exception, SQLException {
        if (logger.isDebugEnabled()) {
            logger.methodLog("deleteAllDisAgrInfo", new Object[]{actId}, Constant.METHOD_START);
        }

        // SQL文実行用
        PreparedStatement pstmt = null;
        // 実行SQL文
        StringBuffer strSql = new StringBuffer();

        try {

            /** 2016/05/18 王　新 ADA IAライン対応Phase2　1.1 CUSEXTでのデータ登録先変更 ADD Begin */
            //strSql.append("delete from ").append(Constant.TABLE_SCHEMA).append(".").append("T_MST_CUS_LDS_AGRMNT_INFO_EXT where ACT_SFDC_ID = ?");
            strSql.append("delete from ").append(Constant.TABLE_SCHEMA).append(".").append("T_MST_CUS_LDS_AGRMNT_INFO_DT where ACT_SFDC_ID = ?");
            /** 2016/05/18 王　新 ADA IAライン対応Phase2　1.1 CUSEXTでのデータ登録先変更 ADD Begin */
            // 実行するSQL文をログに出力
            logger.printSql(strSql.toString());

            // SQL文設定
            pstmt = conn.prepareStatement(strSql.toString());
            pstmt.setString(1, actId);

            // 不成約情報削除
            pstmt.executeUpdate();

        } catch (SQLException sqle) {
            logger.error("E0002", sqle);
            throw sqle;
        } catch (Exception e) {
            logger.error("E0003", e);
            throw e;
        } finally {
            try {
                close(null, pstmt, null);
            } finally {
                pstmt = null;
                conn = null;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.methodLog("deleteAllDisAgrInfo", Constant.METHOD_END);
        }
    }

    /**
     * 不成約情報を取得するSQL分を作成。
     * @param isDetail 不成約情報詳細の取得有無。true-不成約情報詳細、false-不成約情報リスト
     * @param isBranch true - 支社マLeads、false - AG本部Leads
     * @return String 取得用SQL
     */
    private String getDisAgrInfoSql(boolean isDetail, boolean isBranch) {
        StringBuffer sqlSb = new StringBuffer();
        sqlSb.append("select ");
        sqlSb.append("  AGRMNT_NO ");                                                           // 不成約No
        sqlSb.append("  ,COMPE_ATER_CMPNY1 ");                                                  // 競合他社1
        sqlSb.append("  ,COMPE_ATER_CMPNY1_PRDCT ");                                            // 競合他社1商品
        sqlSb.append("  ,MEMO ");                                                               // その他メモ
        if (isDetail) {
            sqlSb.append("  ,FORMAT(AGR.ENTRY_DT, 'yyyy/MM/dd HH:mm') AS ENTRY_DT ");        // 登録日時
            sqlSb.append("  ,FORMAT(AGR.UPDATE_DT, 'yyyy/MM/dd HH:mm') AS UPDATE_DT ");      // 更新日時
            sqlSb.append("  ,ACT.ACT_SUBJECT ");                                                // 活動件名
            sqlSb.append("  ,AGR.HOUSEHOLD_ID ");                                               // 関連顧客ＩＤ
            sqlSb.append("  ,TEL_NG ");                                                         // 連絡が取れない・面談ができない
            sqlSb.append("  ,CNFDNC_NG ");                                                      // その他
            sqlSb.append("  ,FRM_ACQUAINTANCE_APPS ");                                          // 他募集人もしくは通販より契約
            sqlSb.append("  ,UNDERTAKE_NG ");                                                   // 引受が不可能である
            sqlSb.append("  ,COMPE_ATER_CMPNY2 ");                                              // 競合他社2
            sqlSb.append("  ,COMPE_ATER_CMPNY2_PRDCT ");                                        // 競合他社2商品
            sqlSb.append("  ,COMPE_ATER_CMPNY3 ");                                              // 競合他社3
            sqlSb.append("  ,COMPE_ATER_CMPNY3_PRDCT ");                                        // 競合他社3商品
            sqlSb.append("  ,AGR.ENTRY_PG_ID ");                                                // 登録プログラムID
            sqlSb.append("  ,AGR.ENTRY_BY ");                                                   // 登録者ID
            sqlSb.append("  ,AGR.UPDATE_PG_ID ");                                               // 更新プログラムID
            sqlSb.append("  ,AGR.UPDATE_BY ");                                                  // 更新者ID
        } else {
            sqlSb.append("  ,FORMAT(AGR.ENTRY_DT, 'yyyy/MM/dd') AS ENTRY_DT ");                // 登録日時
            sqlSb.append("  ,FORMAT(AGR.UPDATE_DT, 'yyyy/MM/dd') AS UPDATE_DT ");              // 更新日時
        }
        sqlSb.append("from ");

        /** 2016/05/18 王　新 ADA IAライン対応Phase2　1.1 CUSEXTでのデータ登録先変更 ADD Begin */
        //        if(isBranch) {
        //            sqlSb.append(Constant.TABLE_SCHEMA).append(".").append("T_MST_CUS_LDS_AGRMNT_INFO_EXT AGR,");
        //            sqlSb.append(Constant.TABLE_SCHEMA).append(".").append("T_MST_CUS_ACT_DT_EXT ACT ");
        //        } else {
        //            sqlSb.append(Constant.TABLE_SCHEMA).append(".").append("T_MST_CUS_LDS_AGRMNT_INFO_DT AGR,");
        //            sqlSb.append(Constant.TABLE_SCHEMA).append(".").append("T_MST_CUS_ACT_DT ACT ");
        //        }
        sqlSb.append(Constant.TABLE_SCHEMA).append(".").append("T_MST_CUS_LDS_AGRMNT_INFO_DT AGR,");
        sqlSb.append(Constant.TABLE_SCHEMA).append(".").append("T_MST_CUS_ACT_DT ACT ");
        /** 2016/05/18 王　新 ADA IAライン対応Phase2　1.1 CUSEXTでのデータ登録先変更 ADD Begin */
        sqlSb.append("where ");
        sqlSb.append("  ACT.SFDC_ID = AGR.ACT_SFDC_ID");
        if (isBranch) {
            sqlSb.append("  AND ACT.REGIST_KBN = '1'");
        } else {
            sqlSb.append("  AND ACT.REGIST_KBN = '0'");
        }
        if (isDetail) {
            // 成約Noで成約詳細取得
            sqlSb.append("  AND AGRMNT_NO = ?");
        } else {
            // 活動情報キーと紐付く情報取得
            sqlSb.append("  AND ACT_SFDC_ID = ?");
            sqlSb.append("  AND AGR.AGRMNT_NAGRMNT = '不成約'");
        }
        sqlSb.append(" order by AGR.ENTRY_DT");

        return sqlSb.toString();
    }
}
