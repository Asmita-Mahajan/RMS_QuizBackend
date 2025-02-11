package com.app.entity;


import jakarta.persistence.Entity;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;


@Entity
@Data
public class JobDescription {

    @Id
    private ObjectId id = new ObjectId();

    private String filename;

    private String jdcontent;
}