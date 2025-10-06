package com.mytest.springdemo.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mytest.springdemo.model.Employee;

@RestController
public class TestController {

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET)
    public Employee firstPage(@PathVariable Integer id) {

        Employee emp = new Employee();
        if (id==1){
            emp.setName("emp1");
            emp.setDesignation("manager");
            emp.setEmpId("1");
            emp.setSalary(3000);
        }else if(id==2){
            emp.setName("emp2");
            emp.setDesignation("individual contributor");
            emp.setEmpId("2");
            emp.setSalary(2000);
        }

        return emp;
    }

    @RequestMapping(value = "/employee/list", method = RequestMethod.GET)
    public Employee[] firstPage() {

        Employee emp[] = new Employee[3]; 
        
        emp[0]= new Employee();
        
        emp[0].setName("emp1");
        emp[0].setDesignation("manager");
        emp[0].setEmpId("1");
        emp[0].setSalary(3000);
        emp[1]= new Employee();
        emp[1].setName("emp2");
        emp[1].setDesignation("individual contributor");
        emp[1].setEmpId("2");
        emp[1].setSalary(2000);
        emp[2] = new Employee();
        emp[2].setName("emp3");
        emp[2].setDesignation("individual contributor");
        emp[2].setEmpId("3");
        emp[2].setSalary(2000);

        return emp;
    }
}
