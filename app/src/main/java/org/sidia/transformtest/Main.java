package org.sidia.transformtest;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRDrawFrameListener;
import org.gearvrf.GVRMain;
import org.gearvrf.GVRMaterial;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.scene_objects.GVRCubeSceneObject;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Main extends GVRMain {

    private GVRSceneObject base1;
    private GVRSceneObject base2;
    private GVRSceneObject root;
    private GVRSceneObject pivot;

    private GVRSceneObject board1;
    private GVRSceneObject board2;

    @Override
    public void onInit(GVRContext gvrContext) {
        GVRScene scene = gvrContext.getMainScene();
        scene.setBackgroundColor(0.5f, 0.5f, 0.5f, 1f);

        pivot = new GVRSceneObject(gvrContext);
        pivot.getTransform().setScale(0.5f, 0.5f, 0.5f);

        createBases(gvrContext);
        pivot.addChildObject(base1);
        pivot.addChildObject(base2);

        root = new GVRSceneObject(gvrContext);

        // If the next line is commented the problem will not occur
        root.getTransform().setRotationByAxis(90f, 1f, 0f, 0f);
        root.getTransform().setPosition(0f, -5f, -35f);
        root.addChildObject(pivot);

        board1 = createBoard(gvrContext);
        board2 = createBoard(gvrContext);

        scene.addSceneObject(root);
        scene.addSceneObject(board1);
        scene.addSceneObject(board2);

        gvrContext.registerDrawFrameListener(drawFrameListener);
    }

    private void createBases(GVRContext gvrContext) {
        GVRMaterial red = new GVRMaterial(gvrContext, GVRMaterial.GVRShaderType.Phong.ID);
        red.setDiffuseColor(1f, 0f, 0f, 1f);
        base1 = new GVRCubeSceneObject(gvrContext, true);
        base1.getRenderData().setMaterial(red);
        base1.getRenderData().setAlphaBlend(true);
        base1.getTransform().setPosition(-20f, 0f, 0f);
        base1.getTransform().setScale(10f, 1f, 10f);

        GVRMaterial blue = new GVRMaterial(gvrContext, GVRMaterial.GVRShaderType.Phong.ID);
        blue.setDiffuseColor(0f, 0f, 1f, 1f);
        base2 = new GVRCubeSceneObject(gvrContext, true);
        base2.getRenderData().setMaterial(blue);
        base2.getRenderData().setAlphaBlend(true);
        base2.getTransform().setPosition(20f, 0f, 0f);
        base2.getTransform().setScale(10f, 1f, 10f);
    }

    private GVRSceneObject createBoard(GVRContext gvrContext) {
        GVRMaterial green = new GVRMaterial(gvrContext, GVRMaterial.GVRShaderType.Phong.ID);
        green.setDiffuseColor(0f, 1f, 0f, 1f);
        GVRSceneObject box = new GVRCubeSceneObject(gvrContext, true);
        box.getRenderData().setMaterial(green);
        box.getRenderData().setAlphaBlend(true);
        box.getTransform().setScale(0.3f, 1.1f, 0.3f);

        GVRSceneObject board = new GVRSceneObject(gvrContext);
        board.addChildObject(box);

        return board;
    }

    // This procedure should have same results as the next one
    private void followBase1(GVRSceneObject base, GVRSceneObject board) {
        Matrix4f targetMat = base.getTransform().getModelMatrix4f();

        board.getTransform().setModelMatrix(targetMat);
    }

    private void followBase2(GVRSceneObject base, GVRSceneObject board) {
        Matrix4f targetMat = base.getTransform().getModelMatrix4f();

        Vector3f scale = new Vector3f();
        Vector3f pos = new Vector3f();
        Quaternionf rot = new Quaternionf();
        targetMat.getScale(scale);
        targetMat.getTranslation(pos);
        targetMat.normalize3x3();
        rot.setFromNormalized(targetMat);
        board.getTransform().setScale(scale.x, scale.y, scale.z);
        board.getTransform().setPosition(pos.x, pos.y, pos.z);
        board.getTransform().setRotation(rot.w, rot.x, rot.y, rot.z);
    }

    private GVRDrawFrameListener drawFrameListener = new GVRDrawFrameListener() {
        @Override
        public void onDrawFrame(float t) {
            pivot.getTransform().rotate(0.9999619f, 0f, 0.0087265f, 0f);

            followBase1(base1, board1);
            followBase2(base2, board2);
        }
    };
}
