package eni.it.gsrestservice.entities.mapper;


import eni.it.gsrestservice.entities.postgres.QsUser;
import eni.it.gsrestservice.entities.postgres.QsUsersAttrib;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

/**
 * @author Zio Arnold aka Arni
 * @created 12/04/2025 - 17:40 </br>
 */
@Setter
@Getter
@Component
public class QsUsersAttrMapper {
    private String userId;
    private String name;
    private String userIsActive;
    private OffsetDateTime userDataLastModify;
    private String type;
    private String value;
    private OffsetDateTime attributeDataLastModify;
    private String userNewRole;

    public QsUsersAttrMapper map(QsUser qsUser, QsUsersAttrib qsUsersAttrib, String userRoleByUserId) {
        QsUsersAttrMapper qsUsersAttrMapper = new QsUsersAttrMapper();
        qsUsersAttrMapper.setUserId(qsUser.getUserid());
        qsUsersAttrMapper.setName(qsUser.getName());
        qsUsersAttrMapper.setUserIsActive(qsUser.getUserIsActive());
        qsUsersAttrMapper.setUserDataLastModify(qsUser.getDataLastModify());
        qsUsersAttrMapper.setType(qsUsersAttrib.getType());
        qsUsersAttrMapper.setValue(qsUsersAttrib.getValue());
        qsUsersAttrMapper.setAttributeDataLastModify(qsUsersAttrib.getDataLastModify());
        qsUsersAttrMapper.setUserNewRole(userRoleByUserId);
        return qsUsersAttrMapper;
    }
}
