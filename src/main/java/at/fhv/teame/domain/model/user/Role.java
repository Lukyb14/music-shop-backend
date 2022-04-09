package at.fhv.teame.domain.model.user;

public enum Role {
    ADMINISTRATOR("Administrator"),
    SELLER("Seller"),
    OPERATOR("Operator");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return this.roleName;
    }
}
