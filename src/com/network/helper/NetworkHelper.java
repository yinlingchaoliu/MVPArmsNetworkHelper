package com.network.helper;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.network.helper.action.*;
import com.network.helper.common.PsiFileUtils;
import com.network.helper.entity.NetWorkEntity;
import com.network.helper.ui.ArmNetworkForm;
import com.network.helper.ui.ToastUtil;

/**
 * Arm Mvp 帮助工具
 * 自动生成网络代码
 * 补充对应Model Present View层
 * 便于快速开发，专注于UI界面
 * @author chentong
 * @date 2019-3-6
 */
public class NetworkHelper extends AnAction {

    //1、选择
    //2、弹框补充信息
    //3、生成对应模板
    //4、动态插入代码
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        Editor editor = anActionEvent.getData(DataKeys.EDITOR);
        //获得psifile
        PsiFile psiFile = anActionEvent.getData(DataKeys.PSI_FILE);

        String selectedText = PsiFileUtils.getSelectedText(editor);
        ToastUtil.showInfo(project, selectedText);

        ArmNetworkForm armNetworkForm = new ArmNetworkForm(selectedText, project);
        armNetworkForm.setCallBack(new ArmNetworkForm.CallBack() {
            @Override
            public void writeAction(NetWorkEntity entity) {
                try {
                    bean(entity,psiFile);
                    api(entity, psiFile);
                    model(entity, psiFile);
                    fragment(entity,psiFile);
                    activity(entity,psiFile);
                    contract(entity,psiFile);
                    presenter(entity,psiFile);
                } catch (Exception e) {
                    ToastUtil.show(e.getMessage());
                }
            }
        });

        armNetworkForm.pack();
        armNetworkForm.setVisible(true);

    }

    private void api(NetWorkEntity entity, PsiFile psiFile) {
        PsiFile findPsiFile = PsiFileUtils.getFileByName(psiFile, "Api.java");
        new ApiDataWriter(findPsiFile, entity).execute();
    }

    private void model(NetWorkEntity entity, PsiFile psiFile) {
        PsiFile findPsiFile = PsiFileUtils.getFileByName(psiFile, entity.getBizModule().trim()+"Model.java");
        new ModelDataWriter(findPsiFile, entity).execute();
    }

    private void contract(NetWorkEntity entity, PsiFile psiFile) {
        PsiFile findPsiFile = PsiFileUtils.getFileByName(psiFile, entity.getBizModule().trim()+"Contract.java");
        new ContractDataWriter(findPsiFile, entity).execute();
    }

    private void presenter(NetWorkEntity entity, PsiFile psiFile) {
        PsiFile findPsiFile = PsiFileUtils.getFileByName(psiFile, entity.getBizModule().trim()+"Presenter.java");
        new PresenterDataWriter(findPsiFile, entity).execute();
    }

    private void fragment(NetWorkEntity entity, PsiFile psiFile) {
        try {
            PsiFile findPsiFile = PsiFileUtils.getFileByName(psiFile, entity.getBizModule().trim()+"Fragment.java");
            new UIDataWriter(findPsiFile, entity).execute();
        }catch (Exception e){

        }
    }

    private void activity(NetWorkEntity entity, PsiFile psiFile) {
        try {
            PsiFile findPsiFile = PsiFileUtils.getFileByName(psiFile, entity.getBizModule().trim()+"Activity.java");
            new UIDataWriter(findPsiFile, entity).execute();
        }catch (Exception e){
        }
    }

    private void bean(NetWorkEntity entity, PsiFile psiFile) {
        new BeanDataWriter(psiFile, entity).execute();
    }
}