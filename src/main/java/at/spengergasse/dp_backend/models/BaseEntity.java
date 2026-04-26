package at.spengergasse.dp_backend.models;


import jakarta.persistence.*;
import lombok.Getter;

@Getter
@MappedSuperclass
public class BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    public boolean isTransient(){return id == null;}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        if(this.isTransient()){return false;}
        BaseEntity that = (BaseEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public void setId(Long id) {
        this.id = id;
    }
}