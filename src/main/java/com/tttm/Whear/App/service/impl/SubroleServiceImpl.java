package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.SubRole;
import com.tttm.Whear.App.enums.ESubRole;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.SubRoleRepository;
import com.tttm.Whear.App.service.SubroleService;
import com.tttm.Whear.App.utils.request.SubRoleRequest;
import com.tttm.Whear.App.utils.response.SubroleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubroleServiceImpl implements SubroleService {

    private final SubRoleRepository subRoleRepository;

    @Override
    public SubroleResponse createSubRole(SubRoleRequest subRoleRequest) throws CustomException {
        if (subRoleRequest.getSubRoleName() == null ||
                subRoleRequest.getNumberOfClothes() == null ||
                subRoleRequest.getNumberOfCollection() == null ||
                subRoleRequest.getPrice() == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }

        SubRole subRole = SubRole
                .builder()
                .subRoleName(ESubRole.valueOf(subRoleRequest.getSubRoleName().toUpperCase()))
                .numberOfClothes(subRoleRequest.getNumberOfClothes())
                .numberOfCollection(subRoleRequest.getNumberOfCollection())
                .price(subRoleRequest.getPrice())
                .build();
        subRoleRepository.save(subRole);

        return convertToResponse(subRole);
    }

    @Override
    public SubRole updateSubrole(SubRole newSubrole) throws CustomException {
        if (newSubrole == null || newSubrole.getSubRoleID() == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        SubRole subRole = subRoleRepository.getSubRolesBySubRoleID(newSubrole.getSubRoleID());

        if (subRole == null) {
            throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
        }

        subRole.setSubRoleName(newSubrole.getSubRoleName());
        subRole.setNumberOfClothes(newSubrole.getNumberOfClothes());
        subRole.setNumberOfCollection(newSubrole.getNumberOfCollection());
        subRole.setPrice(newSubrole.getPrice());

        subRoleRepository.save(subRole);

        return subRole;
    }

    @Override
    public SubRole getSubroleByID(Integer subroleID) throws CustomException {
        SubRole subRole = subRoleRepository.getSubRolesBySubRoleID(subroleID);
        if (subRole == null) {
            throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
        }
        return subRole;
    }

    @Override
    public SubroleResponse convertToResponse(SubRole subRole) {
        return SubroleResponse
                .builder()
                .subRoleID(subRole.getSubRoleID())
                .subRoleName(subRole.getSubRoleName())
                .numberOfCollection(subRole.getNumberOfCollection())
                .numberOfClothes(subRole.getNumberOfClothes())
                .price(subRole.getPrice())
                .build();
    }

    @Override
    public SubRole getSubroleBySubroleName(ESubRole eSubRole) {
        return subRoleRepository.getSubRolesBySubRoleName(eSubRole);
    }
}
