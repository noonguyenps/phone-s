package project.phoneshop.mservice.models;

import project.phoneshop.mservice.enums.ConfirmRequestType;

public class ConfirmResponse extends Response {
    private Long amount;
    private Long transId;
    private String requestId;
    private ConfirmRequestType requestType;
}
