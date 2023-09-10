package com.sd.shapyfy.infrastructure.services.postgres.sessions.converter;

import com.sd.shapyfy.domain.configuration.SessionService;
import com.sd.shapyfy.domain.configuration.SessionService.CreateSessionRequestParams.CreateSessionPartRequestParams;
import com.sd.shapyfy.domain.configuration.SessionService.CreateSessionRequestParams.CreateSessionPartRequestParams.CreateTrainingExerciseRequestParams;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator.PlanConfiguration.SessionDayConfiguration;
import com.sd.shapyfy.domain.plan.TrainingPlanCreator.PlanConfiguration.SessionDayConfiguration.SelectedExercise;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.PostgresConfigurationAttributesRepository;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.model.ConfigurationAttributeEntity;
import com.sd.shapyfy.infrastructure.services.postgres.configuration.model.ConfigurationAttributeType;
import com.sd.shapyfy.infrastructure.services.postgres.exercises.component.PostgresExerciseFetcher;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.component.SessionPartCreationParams;
import com.sd.shapyfy.infrastructure.services.postgres.trainings.component.SessionPartCreationParams.SelectedExercisesParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreationParamsConverter {

    private final PostgresExerciseFetcher exerciseFetcher;

    private final PostgresConfigurationAttributesRepository configurationAttributesRepository;//TODO use fetcher//service

    public List<SessionPartCreationParams> convert(SessionService.CreateSessionRequestParams requestParams) {

        return requestParams.createSessionPartRequestParams()
                .stream().map(this::convertToPartCreationParams).toList();
    }

    private SessionPartCreationParams convertToPartCreationParams(CreateSessionPartRequestParams requestParams) {

        List<ConfigurationAttributeEntity> attributes = requestParams.createAttributeRequestParams()
                .stream().map(attributeId -> configurationAttributesRepository.findById(attributeId.attributeId().getValue()).get()).toList();

        return new SessionPartCreationParams(
                requestParams.name(),
                requestParams.type(),
                requestParams.state(),
                requestParams.date(),
                requestParams.configurationPartId().getValue(),
                requestParams.createTrainingExerciseRequestParams().stream().map(exerciseRequestParams -> convertToExerciseCreationParams(exerciseRequestParams, attributes)).toList()
        );
    }

    private SelectedExercisesParams convertToExerciseCreationParams(CreateTrainingExerciseRequestParams requestParams, List<ConfigurationAttributeEntity> attributeRequestParams) {
        //TODO Refactor :)
        List<SelectedExercisesParams.SetConfiguration> setConfigurations = new ArrayList<>();
        for (int iterator = 0; iterator < requestParams.sets(); iterator++) {
            setConfigurations.add(new SelectedExercisesParams.SetConfiguration(
                    requestParams.reps(),
                    requestParams.weight(),
                    false,
                    attributeRequestParams.stream().filter(a -> a.getConfigurationAttributeType() == ConfigurationAttributeType.SET).toList()
            ));
        }

        return new SelectedExercisesParams(
                exerciseFetcher.fetchFor(requestParams.exerciseId()),
                requestParams.breakBetweenSets(),
                attributeRequestParams.stream().filter(a -> a.getConfigurationAttributeType() == ConfigurationAttributeType.EXERCISE).toList(),
                setConfigurations);
    }
}
