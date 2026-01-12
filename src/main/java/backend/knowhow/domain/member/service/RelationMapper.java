package backend.knowhow.domain.member.service;

import backend.knowhow.domain.member.domain.RelationType;

public class RelationMapper {

    public static RelationType reverse(RelationType type) {
        return switch (type) {
            case PARENT -> RelationType.CHILD;
            case CHILD -> RelationType.PARENT;
            case GRANDPARENT -> RelationType.GRANDCHILD;
            case GRANDCHILD -> RelationType.GRANDPARENT;
            case CAREGIVER, FRIEND, CUSTOM -> type;
        };
    }
}
