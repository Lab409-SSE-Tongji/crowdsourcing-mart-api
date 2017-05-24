package com.crazy.service.impl;

import com.crazy.entity.Account;
import com.crazy.entity.Requirement;
import com.crazy.repository.RequirementRepository;
import com.crazy.service.RequirementService;
import com.crazy.util.ResJsonTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jieping on 2017-05-24.
 */
@Service
public class RequirementServiceImpl  implements RequirementService{
    @Autowired
    private RequirementRepository requirementRepository;
    @Override
    public ResJsonTemplate addRequirement(Account account, String requirement_name, String requirement_type, int need_manager, Date start_time, Date end_time, String requirement_detail, MultipartFile file) throws IOException {
        Requirement requirement = new Requirement();
        requirement.setCreatorId(account.getAccount_id());
        requirement.setRequirement_name(requirement_name);
        requirement.setRequirement_type(requirement_type);
        requirement.setRequirement_detail(requirement_detail);
        requirement.setNeed_manager(need_manager);
        requirement.setStart_time(start_time);
        requirement.setEnd_time(end_time);
        if (file != null) {
            byte[] data = new byte[file.getInputStream().available()];
            file.getInputStream().read(data);
            requirement.setFile(data);
        }

        requirementRepository.save(requirement);
        return new ResJsonTemplate("201", "创建需求成功");
    }

    @Override
    public ResJsonTemplate getReuirement(Account account) {
        List<Requirement> requirements = requirementRepository.findByCreatorId(account.getAccount_id());
        ArrayList<simpleRequirement> simpleRequirements = new ArrayList<simpleRequirement>();
        for (int i = 0; i < requirements.size(); i++) {
            simpleRequirement s = new simpleRequirement();
            s.setRequirement_id(requirements.get(i).getId());
            s.setRequirement_type(requirements.get(i).getRequirement_type());
            s.setRequirement_name(requirements.get(i).getRequirement_name());
            s.setRequirement_state(requirements.get(i).getRequirement_state());
            simpleRequirements.add(s);
        }
        return new ResJsonTemplate("200", simpleRequirements);
    }
}
class simpleRequirement {
    private Long requirement_id;
    private String requirement_name;
    private String requirement_type;
    private int requirement_state;

    public Long getRequirement_id() {
        return requirement_id;
    }

    public void setRequirement_id(Long requirement_id) {
        this.requirement_id = requirement_id;
    }

    public int getRequirement_state() {
        return requirement_state;
    }

    public void setRequirement_state(int requirement_state) {
        this.requirement_state = requirement_state;
    }

    public String getRequirement_type() {
        return requirement_type;
    }

    public void setRequirement_type(String requirement_type) {
        this.requirement_type = requirement_type;
    }

    public String getRequirement_name() {
        return requirement_name;
    }

    public void setRequirement_name(String requirement_name) {
        this.requirement_name = requirement_name;
    }
}
