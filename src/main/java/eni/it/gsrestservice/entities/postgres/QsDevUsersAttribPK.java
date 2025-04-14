package eni.it.gsrestservice.entities.postgres;


import lombok.*;

import java.io.Serializable;

/**
 * @author Zio Arnold aka Arni
 * @created 14/04/2025 - 12:31 </br>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class QsDevUsersAttribPK implements Serializable {
    private Long id;
}
