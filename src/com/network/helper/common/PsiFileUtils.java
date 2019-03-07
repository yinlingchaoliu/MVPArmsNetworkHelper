package com.network.helper.common;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.EverythingGlobalScope;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiTreeUtil;
import com.network.helper.entity.NetWorkEntity;
import com.network.helper.ui.ToastUtil;

/**
 * http://www.jetbrains.org/intellij/sdk/docs/welcome.html
 * psi文件操作
 * http://www.jetbrains.org/intellij/sdk/docs/basics/architectural_overview/psi_files.html?search=psi
 *
 * @author chentong
 * 重新编写Psi文件便于开发
 */
public class PsiFileUtils {


    /**
     *  psi file 程序结构接口文件
     *
     *
     */

    /**
     * 获得psi文件
     *
     * @param event
     * @return
     */
    public static PsiFile getPsiFile(AnActionEvent event) {
        return event.getData(DataKeys.PSI_FILE);
    }

    /**
     * 通过当前页面元素，获得当前文件
     *
     * @param psiElement
     * @return
     */
    public static PsiFile getInsidePsiFile(PsiElement psiElement) {
        return psiElement.getContainingFile();
    }

    /**
     * 获得project
     *
     * @param event
     * @return
     */
    public static Project getProject(AnActionEvent event) {
        return event.getData(PlatformDataKeys.PROJECT);
    }


    /**
     * 获得editor
     *
     * @param event
     * @return
     */
    public static Editor getEditor(AnActionEvent event) {
        return event.getData(DataKeys.EDITOR);
    }

    /**
     * 获得element
     *
     * @param event
     * @return
     */
    public static PsiElement getPsiElement(AnActionEvent event) {
        return event.getData(LangDataKeys.PSI_ELEMENT);
    }

    public static PsiElement getPsiElement(Editor editor, PsiFile psiFile) {
        if (editor == null || psiFile == null) {
            return null;
        }
        CaretModel caret = editor.getCaretModel();
        PsiElement psiElement = psiFile.findElementAt(caret.getOffset());
        return psiElement;
    }

    /**
     * 获得对应method
     *
     * @param element
     * @return
     */
    public static PsiMethod getPsiMethod(PsiElement element) {
        return PsiTreeUtil.getParentOfType(element, PsiMethod.class);
    }

    /**
     * 获得psiclass
     *
     * @param psiMethod
     * @return
     */
    public static PsiClass getPsiClass(PsiMethod psiMethod) {
        return psiMethod.getContainingClass();
    }

    public static PsiClass getPsiClass(PsiFile psiFile) {
//        GlobalSearchScope globalSearchScope = GlobalSearchScope.fileScope(psiFile);
        String fullName = psiFile.getName();
        String className = fullName.split("\\.")[0];
        PsiClass[] psiClasses = PsiShortNamesCache.getInstance(psiFile.getProject()).getClassesByName(className, new EverythingGlobalScope(psiFile.getProject()));
//        String className1 = fullName.split("\\.")[0];
        return psiClasses[0];
    }

    public static PsiClass getPsiClassExt(PsiFile psiFile){
        String fullName = psiFile.getName();
        String className = fullName.split("\\.")[0];
        return JavaPsiFacade.getInstance(psiFile.getProject()).findClass(className, new EverythingGlobalScope(psiFile.getProject()));
    }

//    /**
//     * 根据名字找文件
//     *
//     * @param psiElement
//     * @param fileName
//     * @return
//     */
//    public static PsiFile getFileByName(PsiElement psiElement, String fileName) {
//        Module moduleForPsiElement = ModuleUtil.findModuleForPsiElement(psiElement);
//        if (moduleForPsiElement != null) {
//            GlobalSearchScope searchScope = GlobalSearchScope.moduleScope(moduleForPsiElement);
//            Project project = psiElement.getProject();
//            PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, fileName, searchScope);
//            if (psiFiles.length != 0) {
//                return psiFiles[0];
//            }
//        }
//        return null;
//    }

    public static PsiFile getFileByName(PsiFile psiFile, String fileName) {
        Project project = psiFile.getProject();
        PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, fileName, new EverythingGlobalScope(psiFile.getProject()));
        if (psiFiles.length != 0) {
            return psiFiles[0];
        }
        return null;
    }

    /**
     * 操作工厂
     *
     * @param psiFile
     * @return
     */
    public static PsiElementFactory getFactory(PsiFile psiFile) {
        return PsiElementFactory.SERVICE.getInstance(psiFile.getProject());
    }

    public static PsiElementFactory getFactory(Project project) {
        return PsiElementFactory.SERVICE.getInstance(project);
    }


    public static PsiElement getPsiElementByEditor(Editor editor, PsiFile psiFile) {
        if (editor == null || psiFile == null) {
            return null;
        }
        CaretModel caret = editor.getCaretModel();
        PsiElement psiElement = psiFile.findElementAt(caret.getOffset());
        if (psiElement != null) {
            if (psiElement.getParent().getText().startsWith("R.layout.")) {
                return psiElement;
            }
        }
        ToastUtil.showError(psiFile.getProject(), "No Layout Found");
        return null;
    }


    public static String getPsiCaretElementText(Editor editor, PsiFile psiFile) {
        if (editor == null || psiFile == null) {
            return null;
        }
        CaretModel caret = editor.getCaretModel();

        PsiElement psiElement = psiFile.findElementAt(caret.getOffset());
        return psiElement.getText();
    }


    public static String getSelectedText(Editor editor) {
        if (editor == null) {
            return null;
        }
        return editor.getSelectionModel().getSelectedText();
    }

    public static boolean isActivity(PsiFile psiFile, PsiClass psiClass) {
        GlobalSearchScope scope = GlobalSearchScope.allScope(psiFile.getProject());
        PsiClass activityClass = JavaPsiFacade.getInstance(psiFile.getProject()).findClass(
                "android.app.Activity", scope);
        return activityClass != null && psiClass.isInheritor(activityClass, false);
    }

    /**
     * 是否包含contractView
     *
     * @param psiFile
     * @param psiClass
     * @return
     */
    public static boolean isConstract(PsiFile psiFile, PsiClass psiClass, NetWorkEntity entity) {
        GlobalSearchScope scope = GlobalSearchScope.allScope(psiFile.getProject());

        String contractPath = entity.getPackageName() + "mvp.contract." + entity.getBizModule() + "Contract";
        PsiClass constractClass = JavaPsiFacade.getInstance(psiFile.getProject()).findClass(
                contractPath, scope);
        return constractClass != null && psiClass.isInheritor(constractClass, false);
    }


//    public static void getResIdBeans(PsiFile psiFile, ArrayList<ResIdBean> container) {
//        psiFile.accept(new XmlRecursiveElementVisitor(true) {
//            @Override
//            public void visitXmlTag(XmlTag tag) {
//                super.visitXmlTag(tag);
//                if (tag.getName().equals("include")) {
//                    XmlAttribute layout = tag.getAttribute("layout");
//                    if (layout != null) {
//                        String value = layout.getValue();
//                        if (value != null && value.startsWith("@layout/")) {
//                            String[] split = value.split("/");
//                            String name = split[1];
//                            String xmlName = String.format("%s.xml", name);
//                            PsiFile fileByName = PsiFileUtils.getFileByName(psiFile, xmlName);
//                            getResIdBeans(fileByName, container);
//                        }
//                    }
//                } else {
//                    XmlAttribute attribute = tag.getAttribute("android:id");
//                    if (attribute != null) {
//                        String idValue = attribute.getValue();
//                        if (idValue != null && idValue.startsWith("@+id/")) {
//                            String[] split = idValue.split("/");
//                            String className;
//                            if (tag.getName().startsWith("com.")) {
//                                String[] custom = tag.getName().split("\\.");
//                                className = custom[custom.length - 1];
//                            } else {
//                                className = tag.getName();
//                            }
//                            String id = split[1];
//                            container.add(new ResIdBean(className, id));
//                        }
//                    }
//                }
//            }
//        });
//    }

}
