package com.revature.util;

import com.revature.models.ReimbursementStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ReimbursementStatusAttributeConverter implements AttributeConverter<ReimbursementStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ReimbursementStatus status){
        if(status == null){
            return null;
        }
        switch(status){
            case PENDING:
                return 1;
            case APPROVED:
                return 2;
            case DENIED:
                return 3;
            case CLOSED:
                return 4;
            default:
                throw new IllegalArgumentException("Wrong reimbursement status");
        }
    }

    @Override
    public ReimbursementStatus convertToEntityAttribute(Integer integer){
        if(integer == null){
            return null;
        }
        switch(integer){
            case 1:
                return ReimbursementStatus.PENDING;
            case 2:
                return ReimbursementStatus.APPROVED;
            case 3:
                return ReimbursementStatus.DENIED;
            case 4:
                return ReimbursementStatus.CLOSED;
            default:
                throw new IllegalArgumentException("Wrong reimbursement status integer");
        }
    }
}
