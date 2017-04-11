package de.hybris.electronics.core.services.impl;

import de.hybris.electronics.core.model.RequestEmailModel;
import de.hybris.electronics.core.services.EmailMeService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.apache.commons.collections.ListUtils;

import javax.annotation.Resource;
import java.util.List;

public class EmailMeServiceImpl implements EmailMeService {

@Resource
 private FlexibleSearchService flexibleSearchService;

 public List<RequestEmailModel> getRequestModel()
  {
    String queryString = "SELECT {c.pk} FROM {RequestEmail AS c}";
    SearchResult<RequestEmailModel> result = flexibleSearchService.search(queryString);
    List<RequestEmailModel> list = result.getResult();
    if(!list.isEmpty())
    {
      return list;
    }
    return ListUtils.EMPTY_LIST;
  }

}
