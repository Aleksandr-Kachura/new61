package de.hybris.electronics.core.services.impl;


import de.hybris.electronics.core.services.ElectronicAccountService;
import de.hybris.electronics.core.services.FamilyManageService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import de.hybris.platform.servicelayer.search.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ElectronicAccountServiceImpl implements ElectronicAccountService {
    private static final Logger LOG = Logger.getLogger(ElectronicAccountServiceImpl.class);
    @Resource
    protected ModelService modelService;

    @Resource
    protected MediaService mediaService;

    @Resource
    protected UserService userService;

    @Resource
    protected FamilyManageService familyManageService;

    @Autowired
    FlexibleSearchService flexibleSearchService;

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private WorkflowTemplateService workflowTemplateService;
    @Autowired
    private WorkflowProcessingService workflowProcessingService;


    // upload to me
    @Override
    public Boolean uploadPdfFile(MultipartFile file, CatalogVersionModel catalogVersionModel, String textMedia, String member) {
        if (StringUtils.isEmpty(file.getContentType()) || (!file.getContentType().equals("application/pdf")))
        {
            return false;
        }
        final MediaModel media = modelService.create(MediaModel.class);
        media.setCatalogVersion(catalogVersionModel);
        Long date = new Date().getTime();
        String strDate = date.toString();
        media.setCode(strDate + file.getOriginalFilename());
        List<CustomerModel> list = new ArrayList<>();
        if (member.equals("mySelf")) {
            list.add(((CustomerModel) userService.getCurrentUser()));
        } else {
            CustomerModel customer = familyManageService.getCustomerByUid(member);
            list.add(customer);
        }

        media.setCustomers(list);
        media.setOwner(userService.getCurrentUser());
        media.setTextMedia(textMedia);
        media.setStatus("unattached");
        modelService.save(media);
        try {

            mediaService.setStreamForMedia(media, file.getInputStream(), file.getName(), file.getContentType());
        } catch (final IOException e) {
            LOG.error(e);
        }
        registerMedia(media);
        return true;
    }

    // upload to family member
    @Override
    public void deleteFile(String mediaCode) {

        String queryString = "SELECT {m.pk} FROM {Media AS m} where {m.code} = '" + mediaCode + "'";
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.addQueryParameter("code", mediaCode);
        SearchResult result = flexibleSearchService.search(query);
        List<MediaModel> mediaModel = result.getResult();
        MediaModel media = mediaModel.get(0);
        modelService.remove(media);

    }

    @Override
    public MediaModel getMedia(String mediaCode) {
        String queryString = "SELECT {m.pk} FROM {Media AS m} where {m.code} = '" + mediaCode + "'";
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.addQueryParameter("code", mediaCode);
        SearchResult result = flexibleSearchService.search(query);
        List<MediaModel> mediaModel = result.getResult();
        MediaModel media = mediaModel.get(0);
        return  media;
    }


//    // upload to family member
//    @Override
//    public void deleteFile(String mediaCode) {
//
//        UserModel user = userService.getUserForUID(mediaCode);
//        String queryString = "SELECT {m.pk} FROM {Media AS m} where {m.code} = '" + mediaCode + "'";
//        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
//        query.addQueryParameter("code", mediaCode);
//        SearchResult result = flexibleSearchService.search(query);
//        List<MediaModel> mediaModel = result.getResult();
//        MediaModel media = mediaModel.get(0);
//        media.setStatus("unattached");
//        List<CustomerModel> list = new ArrayList<>();
//        list.addAll(media.getCustomers());
//        list.add((CustomerModel) user);
//        media.setCustomers(list);
//        modelService.save(media);
//
//    }

    /**
     * @param user used for select members by family name
     * @return
     */
    @Override
    public HashMap getFamily(CustomerData user) {
        String queryString = "SELECT {c.pk} FROM {FamilyElectronic AS c} where {c.familyName} = '" + user.getFamilyName() + "'";
        FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        SearchResult result = flexibleSearchService.search(query);
        List<CustomerModel> customers = result.getResult();
        final HashMap customerMap = new HashMap();
        for (CustomerModel customer : customers) {
            customerMap.put(customer.getCustomerID(), customer.getName());

        }
        return customerMap;

    }

    /**
     * @param media current item type
     *              register our workflow
     */
    public void registerMedia(final MediaModel media) {
        final WorkflowTemplateModel workflowTemplate = workflowTemplateService.getWorkflowTemplateForCode("NewAttachRegistration");

        final WorkflowModel workflow = workflowService.createWorkflow(workflowTemplate, media, userService.getAdminUser());
        modelService.save(workflow);
        for (final WorkflowActionModel action : workflow.getActions()) {
            modelService.save(action);
        }

        workflowProcessingService.startWorkflow(workflow);
    }


}
