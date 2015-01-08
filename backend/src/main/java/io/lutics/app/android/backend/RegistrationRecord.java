package io.lutics.app.android.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import lombok.Data;

@Data
@Entity
public class RegistrationRecord {

    @Id
    Long id;

    @Index
    private String regId;
}