package org.exoplatform.addons.social.webui.space;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.addons.service.config.SpaceCategoriesServiceConfig;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.portal.application.PortalRequestContext;
import org.exoplatform.portal.application.RequestNavigationData;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.social.common.router.ExoRouter;
import org.exoplatform.social.common.router.ExoRouter.Route;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;
import org.exoplatform.social.webui.Utils;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormRadioBoxInput;



@ComponentConfig(
    lifecycle = UIFormLifecycle.class,
    template = "war:/groovy/astia/UISpaceCategory.gtmpl",
    events = {
        @EventConfig(listeners = UISpaceCategory.SaveActionListener.class)
      }
)
public class UISpaceCategory extends UIForm {

  final static private String SPACE_CATEGORY = "UICATEGORY";
  private UIComponent uicomponent_;  

  SpaceCategoriesServiceConfig spaceCategories = (SpaceCategoriesServiceConfig) PortalContainer.getInstance().getComponentInstanceOfType(SpaceCategoriesServiceConfig.class);

  public UISpaceCategory() throws Exception {

	  
	  uicomponent_= this.findComponentById("UISpaceCategory");

    List <String> spaceCats =spaceCategories.getspacesCategories();
    int nbCat=spaceCats.size();

    List<SelectItemOption<String>> spaceCategory = new ArrayList<SelectItemOption<String>>(nbCat);
    for (String cat : spaceCats) {
      spaceCategory.add(new SelectItemOption<String>(cat));
    }
    UIFormRadioBoxInput uiRadioRegistration = new UIFormRadioBoxInput(SPACE_CATEGORY, getSpaceByContext().getType(), spaceCategory);
    addUIFormInput(uiRadioRegistration);
  }


  public void setValue(Space space) throws Exception {
    String registration = space.getRegistration();
    ((UIFormRadioBoxInput)getChildById(SPACE_CATEGORY)).setValue(registration);
    
  }

  static public class SaveActionListener extends EventListener<UISpaceCategory> {
    public void execute(Event<UISpaceCategory> event) throws Exception {
      UISpaceCategory uiSpaceCategory = event.getSource();
      SpaceService spaceSrc = uiSpaceCategory.getApplicationComponent(SpaceService.class);
      WebuiRequestContext requestContext = event.getRequestContext();
      String category = ((UIFormRadioBoxInput)uiSpaceCategory.getChildById(SPACE_CATEGORY)).getValue();
      Space space = Utils.getSpaceByContext();
      space.setType(category);
      space.setEditor(Utils.getViewerRemoteId());
      spaceSrc.updateSpace(space);
      UIApplication uiApp = requestContext.getUIApplication();
      uiApp.clearMessages();
      uiApp.addMessage(new ApplicationMessage("success", null, ApplicationMessage.INFO));
      //requestContext.addUIComponentToUpdateByAjax(uiApp.getUIPopupMessages());
      requestContext.addUIComponentToUpdateByAjax(uiSpaceCategory);
    }
  }

  /**
   * @return Space
   */
  private static Space getSpaceByContext() {
    //
    PortalRequestContext pcontext = Util.getPortalRequestContext();
    String requestPath = pcontext.getControllerContext().getParameter(RequestNavigationData.REQUEST_PATH);
    Route route = ExoRouter.route(requestPath);
    if (route == null)
      return null;

    //
    String spacePrettyName = route.localArgs.get("spacePrettyName");
    SpaceService spaceService = (SpaceService) ExoContainerContext.getCurrentContainer()
                                                                  .getComponentInstanceOfType(SpaceService.class);
    return spaceService.getSpaceByPrettyName(spacePrettyName);
  }
  
  public UIComponent getUIComponent() {
   
   
    return  this.findComponentById("UISpaceCategory");
  }
}