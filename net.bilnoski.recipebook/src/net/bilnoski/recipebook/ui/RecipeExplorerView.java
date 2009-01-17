package net.bilnoski.recipebook.ui;

import net.bilnoski.recipebook.db.RecipeDataStore;
import net.bilnoski.recipebook.internal.Activator;
import net.bilnoski.recipebook.model.Cookbook;
import net.bilnoski.recipebook.ui.BasicViewActions.CollapseAllAction;
import net.bilnoski.recipebook.ui.BasicViewActions.ExpandAllAction;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.ViewPart;

public class RecipeExplorerView extends ViewPart
{
   public static final String VIEW_ID = "net.bilnoski.recipebook.views.explorer";
   protected TreeViewer viewer;
   
   @Override
   public void setFocus()
   {
      if (viewer != null)
         viewer.getTree().setFocus();
   }
   
   @Override
   public void createPartControl(Composite parent)
   {
      PatternFilter pf = new PatternFilter();
      pf.setIncludeLeadingWildcard(true);
      FilteredTree ft = new CategorizedFilteredTree(parent);
      ft.setInitialText("<Type Filter Text>");
      viewer = ft.getViewer();
      
      //TODO: parse secondary view ID into filters and columns
      
      //TODO: add content and label providers
      
      IToolBarManager tbm = this.getViewSite().getActionBars().getToolBarManager();
      tbm.add(new ExpandAllAction(viewer));
      tbm.add(new CollapseAllAction(viewer));
      
      IMenuManager mm = getViewSite().getActionBars().getMenuManager();
      mm.add(new LoadAction());
   }
   
   public static class CategorizedFilteredTree extends FilteredTree
   {
      public CategorizedFilteredTree(Composite parent)
      {
         super(parent, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL, new PatternFilter());
      }
      
      @Override
      protected void createControl(Composite parent, int treeStyle)
      {
         GridLayout layout = new GridLayout();
         layout.marginHeight = 0;
         layout.marginWidth = 0;
         setLayout(layout);
         setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

         // always show filter composite
//         if (showFilterControls)
         {
            filterComposite = new Composite(this, SWT.NONE);
            GridLayout filterLayout = new GridLayout(3, false);
            filterLayout.marginHeight = 0;
            filterLayout.marginWidth = 0;
            filterComposite.setLayout(filterLayout);
            filterComposite.setFont(parent.getFont());

            createCategorySelector(filterComposite);
            createFilterControls(filterComposite);
            filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
         }

         treeComposite = new Composite(this, SWT.NONE);
         GridLayout treeCompositeLayout = new GridLayout();
         treeCompositeLayout.marginHeight = 0;
         treeCompositeLayout.marginWidth = 0;
         treeComposite.setLayout(treeCompositeLayout);
         GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
         treeComposite.setLayoutData(data);
         createTreeControl(treeComposite, treeStyle);
      }
      
      private void createCategorySelector(Composite parent)
      {
         //TODO: create category selector child actions
         ToolBarManager tb2 = new ToolBarManager(SWT.FLAT | SWT.HORIZONTAL);
         tb2.createControl(parent);

         IAction catSelAction = new Action("Flat", IAction.AS_DROP_DOWN_MENU) {
            @Override public void run() {
//               clearText();
            }
         };

         catSelAction.setToolTipText("Select Filing System");

         tb2.add(catSelAction);
         tb2.update(false);
      }
   }
   
   private static class LoadAction extends Action 
   {
      public LoadAction()
      {
         super("Load");
      }
      
      @Override
      public void run()
      {
         Cookbook cb = Activator.getDefault().getService(Cookbook.class);
         try {
            new RecipeDataStore().load(cb);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }
}
