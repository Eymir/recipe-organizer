package net.bilnoski.recipebook.ui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class CookbookPerspective implements IPerspectiveFactory
{
   public static final String PERSPECTIVE_ID = "net.bilnoski.recipebook.cookbook.perspective";
   @Override
   public void createInitialLayout(IPageLayout layout)
   {
      String editorArea = layout.getEditorArea();
      layout.setEditorAreaVisible(false);

      layout.addStandaloneView(RecipeExplorerView.VIEW_ID, true, IPageLayout.LEFT, 1.0f, editorArea);
   }
}
