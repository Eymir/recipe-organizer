package net.bilnoski.recipebook.internal.application;

import net.bilnoski.recipebook.ui.CookbookPerspective;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor
{
   @Override
   public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer)
   {
      return new ApplicationWorkbenchWindowAdvisor(configurer);
   }
   
   @Override
   public void preStartup()
   {
      super.preStartup();
      getWorkbenchConfigurer().setSaveAndRestore(true);
   }

   @Override
   public String getInitialWindowPerspectiveId()
   {
      return CookbookPerspective.PERSPECTIVE_ID;
   }
}
