package com.revature.util;

import com.revature.models.ReimbursementType;
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
public class ReimbursementTypeAttributeConverter implements AttributeConverter<ReimbursementType, Integer> {

    private static Logger logger = LogManager.getLogger(ReimbursementStatusAttributeConverter.class);

    /**
     * Converts a reimbursement type enum to a number for the database
     * @param type the reimbursement type enum
     * @return the number it represents in the DB
     */
    @Override
    public Integer convertToDatabaseColumn(ReimbursementType type){
        if(type == null){
            return null;
        }
        switch(type){
            case LODGING:
                return 1;
            case TRAVEL:
                return 2;
            case FOOD:
                return 3;
            case OTHER:
                return 4;
            default:
                logger.error("Wrong reimbursement type enum", new IllegalArgumentException());
                return -1;
        }
    }

    /**
     * Converts an integer grabbed from the database into the reimbursement
     * type enum
     * @param integer the DB representation of the enum
     * @return the enum that the DB integer represents
     */
    @Override
    public ReimbursementType convertToEntityAttribute(Integer integer){
        if(integer == null){
            return null;
        }
        switch(integer){
            case 1:
                return ReimbursementType.LODGING;
            case 2:
                return ReimbursementType.TRAVEL;
            case 3:
                return ReimbursementType.FOOD;
            case 4:
                return ReimbursementType.OTHER;
            default:
                logger.error("Wrong reimbursement type integer", new IllegalArgumentException());
                return null;
        }
    }
}
