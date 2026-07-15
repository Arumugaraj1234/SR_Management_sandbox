package com.vmfg.mis.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class EmployeeEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String empId;
    private String empName;
}
