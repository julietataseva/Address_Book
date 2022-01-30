package nbu.java.services;

import nbu.java.exceptions.NotFoundException;
import nbu.java.model.pojo.dto.AdditionalFieldDTO;
import nbu.java.repositories.AdditionalFieldRepository;
import nbu.java.model.pojo.AdditionalField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdditionalFieldService {
    AdditionalFieldRepository<AdditionalField> additionalFieldRepository;

    @Autowired
    public AdditionalFieldService(AdditionalFieldRepository<AdditionalField> additionalFieldRepository) {
        this.additionalFieldRepository = additionalFieldRepository;
    }

    @Transactional
    public void addAdditionalField(AdditionalField additionalField){

        if (additionalField == null){}

        if(additionalField.getTitle() == null || additionalField.getTitle().length() > 45){ }

        if(additionalField.getText() == null || additionalField.getText().length() > 255){}

        additionalFieldRepository.save(additionalField);
    }

    @Transactional
    public List<AdditionalField> findByContactId(int id) { return (List<AdditionalField>) additionalFieldRepository.findByContactId(id); }

    @Transactional
    public AdditionalField findById(int id) throws NotFoundException {
        AdditionalField additionalField = additionalFieldRepository.findById(id);
        if (additionalField == null) throw new NotFoundException("This additional field doesn't exits!");
        return additionalField;
    }

    @Transactional
    public void deleteAdditionalField(AdditionalField additionalField){ additionalFieldRepository.delete(additionalField); }


    @Transactional
    public void update(int id, AdditionalFieldDTO additionalFieldDTO) {
        AdditionalField additionalField = additionalFieldRepository.findById(id);

        if (additionalFieldDTO.getTitle() != null){
            additionalField.setTitle(additionalFieldDTO.getTitle());
        }

        if (additionalFieldDTO.getText() != null){
            additionalField.setText(additionalFieldDTO.getText());
        }

        additionalFieldRepository.save(additionalField);
    }


}
