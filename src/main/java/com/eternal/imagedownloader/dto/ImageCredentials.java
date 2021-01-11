package com.eternal.imagedownloader.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ImageCredentials {
    String url;
    String fileName;
}
