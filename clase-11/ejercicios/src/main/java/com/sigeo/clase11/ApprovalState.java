package com.sigeo.clase11;

public record ApprovalState(
    String decision,
    String rejectionReason,
    int priority
) {}
