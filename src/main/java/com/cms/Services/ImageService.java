package com.cms.Services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    public String uploadImage(MultipartFile contactImage);

    String getUrlFromPublicId(String publicId);
}
