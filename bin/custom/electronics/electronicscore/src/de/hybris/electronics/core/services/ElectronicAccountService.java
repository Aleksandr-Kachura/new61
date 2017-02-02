package de.hybris.electronics.core.services;


import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.util.HashMap;

import de.hybris.platform.core.model.media.MediaModel;
import org.springframework.web.multipart.MultipartFile;

// interface for electronic service
public interface ElectronicAccountService {

    Boolean uploadPdfFile(MultipartFile file, CatalogVersionModel catalogVersion,String textMedia,String member);

    HashMap getFamily(CustomerData user);

    void deleteFile(String mediaCode);

    MediaModel getMedia(String user);


}
