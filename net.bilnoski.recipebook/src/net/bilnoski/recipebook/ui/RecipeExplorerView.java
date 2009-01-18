package net.bilnoski.recipebook.ui;

import net.bilnoski.recipebook.db.RecipeDataStore;
import net.bilnoski.recipebook.internal.Activator;
import net.bilnoski.recipebook.model.Cookbook;
import net.bilnoski.recipebook.ui.BasicViewActions.CollapseAllAction;
import net.bilnoski.recipebook.ui.BasicViewActions.ExpandAllAction;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
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
      
      getSite().setSelectionProvider(viewer);
      
      //TODO: parse secondary view ID into filters and columns
      
      TreeViewerColumn tvc = new TreeViewerColumn(viewer, SWT.NONE);
      tvc.setLabelProvider(new ColumnLabelProvider());
      tvc.getColumn().setMoveable(true);
      tvc.getColumn().setText("Recipe");
      tvc.getColumn().setWidth(400);
      viewer.getTree().setHeaderVisible(true);
      viewer.getTree().setSortColumn(tvc.getColumn());
      viewer.getTree().setSortDirection(SWT.UP);
      
      viewer.setContentProvider(new CP());
      viewer.setInput(this);
      
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
   
   private static class CP implements ITreeContentProvider
   {
      Object[] EMPTY = new Object[0];
      public Object[] getChildren(Object parentElement)
      {
         return null;
      }

      public Object getParent(Object element)
      {
         return null;
      }

      public boolean hasChildren(Object element)
      {
         return false;
      }

      public Object[] getElements(Object inputElement)
      {
         if (inputElement instanceof Cookbook)
         {
            return ((Cookbook)inputElement).getRecipes().toArray();
         }
         return EMPTY;
      }

      public void dispose()
      {
      }

      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }
   }
   
   private class LoadAction extends Action 
   {
      public LoadAction()
      {
         super("Load");
      }
      
      @Override
      public void run()
      {
         Job j = new Job("Loading Cookbook"){
            @Override
            protected IStatus run(IProgressMonitor monitor)
            {
               monitor.beginTask("Loading Cookbook", IProgressMonitor.UNKNOWN);
               final Cookbook cb = Activator.getDefault().getService(Cookbook.class);
               try {
                  new RecipeDataStore().load(cb);
                  PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable(){
                     public void run()
                     {
                        viewer.setInput(cb);
                        viewer.refresh();
                     }
                  });
                  return Status.OK_STATUS;
               } catch (Exception e) {
                  e.printStackTrace();
               }
               finally
               {
                  monitor.done();
               }
               return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error loading cookbook");
            }
         };
         j.schedule();
      }
   }
}
