public class NoiseReducer {

    Image img;
    Segmentation segmentation;
    float borderProximityThreshold1;
    float borderProximityThreshold2;
    float heightScoreThreshold1;
    float heightScoreThreshold2;
    float dilationPercentage;

    public NoiseReducer(Image _img, float _proximityThreshold1, float _proximityThreshold2, float _heightThreshold1,
            float _heightThreshold2, float _dilationPercentage) {
        img = _img;
        borderProximityThreshold1 = _proximityThreshold1;
        borderProximityThreshold2 = _proximityThreshold2;
        heightScoreThreshold1 = _heightThreshold1;
        heightScoreThreshold2 = _heightThreshold2;
        dilationPercentage = _dilationPercentage;
        segmentation = new Segmentation(img);
        segmentation.segment();
    }

    public Image getCleanImage() {
        getTextComponents(segmentation.getComponents(),
                segmentation.getComponentsCount());
        // segmentation.setComponentsArray(filteredComponents);
        Image denoisedImage = ImageUtility.addComponentsOnImage(segmentation.getComponents(),
                segmentation.getComponentsCount(), img.getWidth(), img.getHeight());
        return denoisedImage;
    }

    public void getTextComponents(Component[] comps, int componentsCount) {
        // Border Proximity Check
        Component[] filteredComponents1 = checkBorderProximity(segmentation.getComponents(),
                segmentation.getComponentsCount());
        segmentation.setComponentsArray(filteredComponents1);

        // Height
        Component[] filteredComponents2 = checkHeightScore(filteredComponents1, segmentation.getComponentsCount());
        segmentation.setComponentsArray(filteredComponents2);

        // Width
        Component[] filteredComponents3 = checkWidthScore(filteredComponents2, segmentation.getComponentsCount());
        segmentation.setComponentsArray(filteredComponents3);

        // Second Pass Border Noise Removal
        Component[] filteredComponents4 = checkBorderProximity2(filteredComponents2, segmentation.getComponentsCount());
        segmentation.setComponentsArray(filteredComponents4);

        // return filteredComponents3;
    }

    public Component[] checkBorderProximity(Component[] comps, int componentsCount) {
        Component[] newComps = new Component[componentsCount];
        int count = 0;

        for (int i = 0; i < componentsCount; i++) {
            int[] compRect = comps[i].getRect(); // minX,minY,width,height
            if (comps[i] == null) {

            } else if ((compRect[0] < (int) (borderProximityThreshold1 * img.getWidth()))
                    || (compRect[1] < (int) (borderProximityThreshold1 * img.getHeight()))
                    || ((compRect[0] + compRect[2]) > (int) ((1 - borderProximityThreshold1) * img.getWidth()))
                    || ((compRect[1]
                            + compRect[3]) > (int) ((1 - borderProximityThreshold1) * img.getHeight()))) {

            } else {
                newComps[count] = comps[i];
                count++;
            }

        }
        return newComps;
    }

    public Component[] checkHeightScore(Component[] comps, int componentsCount) {
        Component[] newComps = new Component[componentsCount];
        int count = 0;

        for (int i = 0; i < componentsCount; i++) {
            int[] compRect = comps[i].getRect(); // minX,minY,width,height
            if ((compRect[3] < (int) (heightScoreThreshold1 * img.getWidth()))) {
                newComps[count] = comps[i];
                count++;
            }
        }
        return newComps;
    }

    public Component[] checkWidthScore(Component[] comps, int componentsCount) {
        Component[] newComps = new Component[componentsCount];
        int count = 0;

        for (int i = 0; i < componentsCount; i++) {
            int[] compRect = comps[i].getRect(); // minX,minY,width,height
            if ((compRect[2] < (int) (heightScoreThreshold1 * img.getWidth()))) {
                newComps[count] = comps[i];
                count++;
            }
        }
        return newComps;
    }

    public Component[] checkBorderProximity2(Component[] comps, int componentsCount) {
        Component[] newComps = new Component[componentsCount];
        int count = 0;

        for (int i = 0; i < componentsCount; i++) {
            int[] compRect = comps[i].getRect(); // minX,minY,width,height
            if ((compRect[0] < (int) (borderProximityThreshold2 * img.getWidth()))
                    || (compRect[1] < (int) (borderProximityThreshold2 * img.getHeight()))
                    || ((compRect[0] + compRect[2]) >= (int) ((1 - borderProximityThreshold2) * img.getWidth()))
                    || ((compRect[1]
                            + compRect[3]) >= (int) ((1 - borderProximityThreshold2) * img.getHeight()))) {

                if ((compRect[2] > (int) (heightScoreThreshold2 * img.getWidth()))
                        || (compRect[3] > (int) (heightScoreThreshold2
                                * img.getHeight()))) {
                } else {
                    newComps[count] = comps[i];
                    count++;
                }

            } else {
                newComps[count] = comps[i];
                count++;
            }

        }
        return newComps;
    }

    public void removeProbableImages(Image denoisedImage) {
        Image dilatedImage = ImageUtility.dilate(img, (int) (img.getWidth() * dilationPercentage), 0);
        Segmentation segment = new Segmentation(dilatedImage);
        segment.segment();
        Component[] heightFilteredComponents = checkHeightScore(segment.getComponents(), segment.getComponentsCount());

        segment.setComponentsArray(heightFilteredComponents);
        ImageUtility.writeImage(dilatedImage, "dilated.png");
        Image heightfilteredImage = ImageUtility.addComponentsOnImage(segment.getComponents(),
                segment.getComponentsCount(), img.getWidth(), img.getHeight());
        denoisedImage.logicalAnd(heightfilteredImage);
    }
}
