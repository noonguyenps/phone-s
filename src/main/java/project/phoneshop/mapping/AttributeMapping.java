package project.phoneshop.mapping;

import project.phoneshop.model.entity.AttributeEntity;
import project.phoneshop.model.entity.AttributeOptionEntity;
import project.phoneshop.model.payload.request.attribute.AttributeOptionRequest;
import project.phoneshop.model.payload.request.attribute.AttributeRequest;

public class AttributeMapping {
    public static AttributeEntity addNewAttribute(AttributeRequest attributeRequest){
        AttributeEntity attribute = new AttributeEntity();
        attribute.setId(attributeRequest.getId());
        attribute.setName(attributeRequest.getName());
        return attribute;
    }
    public static AttributeOptionEntity addNewAttributeOption(AttributeOptionRequest attributeOptionRequest,AttributeEntity attribute){
        AttributeOptionEntity attributeOption = new AttributeOptionEntity();
        attributeOption.setId(attributeOptionRequest.getId());
        attributeOption.setValue(attributeOptionRequest.getValue());
        attributeOption.setIdType(attribute);
        return attributeOption;
    }
}
