package com.example.SpringProject.theatre;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SpringProject.Exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TheatreServiceImpl implements TheatreService {
    
    @Autowired
    private TheatreRepository theatreRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public TheatreDTO createTheatre(TheatreDTO dto) {
        TheatreEntity theatre = modelMapper.map(dto, TheatreEntity.class);
        TheatreEntity saved = theatreRepository.save(theatre);
        return modelMapper.map(saved, TheatreDTO.class);
    }

    @Override
    public List<TheatreDTO> getAllTheatres() {
        return theatreRepository.findAll()
                .stream()
                .map(theatre -> modelMapper.map(theatre, TheatreDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public TheatreDTO getTheatreById(Long id) throws ResourceNotFoundException {
        TheatreEntity theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service.THEATRE_NOT_FOUND"));

        return modelMapper.map(theatre, TheatreDTO.class);
    }

    // Admin Methods
    @Override
    public TheatreEntity create(TheatreDTO dto) {
        return theatreRepository.save(modelMapper.map(dto, TheatreEntity.class));
    }

    @Override
    public TheatreEntity update(Long id, TheatreDTO dto) throws ResourceNotFoundException {
        TheatreEntity theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service.THEATRE_NOT_FOUND"));
        
        theatre.setName(dto.getName());
        theatre.setLocation(dto.getLocation());
        return theatreRepository.save(theatre);
    }

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        Optional<TheatreEntity> theatreOpt = theatreRepository.findById(id);
        if (!theatreOpt.isPresent()) {
            throw new ResourceNotFoundException("Service.THEATRE_NOT_FOUND");
        }
        theatreRepository.deleteById(id);
    }
}

