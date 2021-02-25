package com.revature.util;

import com.revature.models.ReimbursementStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Class used to convert enums in the POJO to integers that can be stored in
 * the database, and vice versa
 * @author Cole Space
 * @author Gabrielle Luna
 */
@Converter(autoApply = true)
public class ReimbursementStatusAttributeConverter implements AttributeConverter<ReimbursementStatus, Integer> {

    private static Logger logger = LogManager.getLogger(ReimbursementTypeAttributeConverter.class);

    /**
     * Converts a reimbursement status enum to a number for the database
     * @param status the reimbursement status enum
     * @return the number it represents in the DB
     */
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
                logger.error("Wrong reimbursement status enum", new IllegalArgumentException());
                return -1;
        }
    }

    /**
     * Converts an integer grabbed from the database into the reimbursement
     * status enum
     * @param integer the DB representation of the enum
     * @return the enum that the DB integer represents
     */
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
                logger.error("Wrong reimbursement status integer", new IllegalArgumentException());
                return null;
        }
    }
}
