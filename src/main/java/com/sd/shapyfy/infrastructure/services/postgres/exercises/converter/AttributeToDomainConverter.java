package com.sd.shapyfy.infrastructure.services.postgres.exercises.converter;

import com.sd.shapyfy.domain.configuration.model.Attribute;
import com.sd.shapyfy.domain.configuration.model.AttributeId;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SessionExerciseAdditionalAttributeEntity;
import com.sd.shapyfy.infrastructure.services.postgres.sessions.model.SetAdditionalAttributeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttributeToDomainConverter {

    public Attribute convert(SessionExerciseAdditionalAttributeEntity attributeEntity) {
        return new Attribute(
                AttributeId.of(attributeEntity.getId()),
                attributeEntity.getName(),
                attributeEntity.getValue()
        );
    }

    public Attribute convert(SetAdditionalAttributeEntity attributeEntity) {
        return new Attribute(
                AttributeId.of(attributeEntity.getId()),
                attributeEntity.getName(),
                attributeEntity.getValue()
        );
    }
}
