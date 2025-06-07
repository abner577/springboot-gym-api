package practice.spring_gym_api.dto.impl;

import practice.spring_gym_api.dto.WorkerDTO;
import practice.spring_gym_api.dto.WorkerMapper;
import practice.spring_gym_api.entity.WorkerEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class WorkerMapperimpl implements WorkerMapper {
    private final ModelMapper modelMapper;

    public WorkerMapperimpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public WorkerEntity convertToWorkerEntity(WorkerDTO workerDTO) {
        return modelMapper.map(workerDTO, WorkerEntity.class);
    }

    @Override
    public WorkerDTO convertToWorkerDTO(WorkerEntity workerEntity) {
        return modelMapper.map(workerEntity, WorkerDTO.class);
    }
}
