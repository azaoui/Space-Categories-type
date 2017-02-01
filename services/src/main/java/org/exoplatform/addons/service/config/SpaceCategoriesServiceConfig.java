package org.exoplatform.addons.service.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.commons.utils.PropertyManager;
import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.container.xml.InitParams;

/**
 * @author azaoui@exoplatform.com
 */
public class SpaceCategoriesServiceConfig extends BaseComponentPlugin {

  private List<String>        spacesCategories = new ArrayList<String>();

  private static final String CATEGORIES_LIST  = "exo.spaces.categories";

  public SpaceCategoriesServiceConfig(InitParams initParams) {

    String categories = StringUtils.isNotBlank(PropertyManager.getProperty(CATEGORIES_LIST)) ? PropertyManager.getProperty(CATEGORIES_LIST)
                                                                                            : initParams.getValueParam("space-categories")
                                                                                                        .getValue();

    spacesCategories = Arrays.asList(categories.split(","));

  }

  public List<String> getspacesCategories() {
    return spacesCategories;
  }

}
