package com.network.helper.action;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.network.helper.common.PsiFileUtils;
import com.network.helper.config.ArmConstant;
import com.network.helper.entity.NetWorkEntity;
import com.network.helper.ui.ToastUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *  Api插入文件
 */
public class ContractDataWriter extends WriteCommandAction {
    private Project project;
    private PsiFile psiFile;
    private PsiClass psiClass;
    private NetWorkEntity entity;

    public ContractDataWriter(@NotNull PsiFile psiFile, NetWorkEntity entity){
        super(psiFile.getProject(),psiFile);
        this.project = psiFile.getProject();
        this.psiFile = psiFile;
        this.psiClass = PsiFileUtils.getPsiClass(psiFile);
        this.entity = entity;
    }

    private ContractDataWriter(@Nullable Project project, PsiFile... files) {
        super(project, files);
    }

    @Override
    protected void run(@NotNull Result result) throws Throwable {
        write();
    }

    private void write(){
        PsiElementFactory factory = PsiFileUtils.getFactory(psiFile);


        PsiClass viewPsiClass = getViewPsiClass();
        PsiClass modelPsiClass = getModelPsiClass();

        String  contractViewStr = ArmConstant.getContractViewStr(entity);
        viewPsiClass.add(factory.createMethodFromText(contractViewStr,viewPsiClass));

        ToastUtil.show(contractViewStr);

        String contractModelStr = ArmConstant.getContractModelStr(entity);
        modelPsiClass.add(factory.createMethodFromText(contractModelStr,modelPsiClass));

//        psiClass.add(viewPsiClass);
//        psiClass.add(modelPsiClass);
        ToastUtil.show(contractModelStr);
    }


    //常用工具
    private boolean isMethodExist(NetWorkEntity entity){
        PsiMethod[] methods = psiClass.findMethodsByName(ArmConstant.getInterfaceMethodName(entity),false);
        PsiMethod findViewsMethod = methods.length > 0 ? methods[0] : null;
        return findViewsMethod != null;
    }

    private PsiClass getViewPsiClass(){
        PsiClass[] interfacePsiClasses =  psiClass.getAllInnerClasses();
        for (PsiClass psiClass : interfacePsiClasses){
            if (psiClass.getName().equals("View") ){
                return psiClass;
            }
        }
        return null;
    }

    private PsiClass getModelPsiClass(){
        PsiClass[] interfacePsiClasses =  psiClass.getAllInnerClasses();
        for (PsiClass psiClass : interfacePsiClasses){
            if (psiClass.getName().equals("Model") ){
                return psiClass;
            }
        }
        return null;
    }

}
