public class NoiseReducer {

    Image img;
    Segmentation segmentation;
    float borderProximityThreshold1;
    float borderProximityThreshold2;
    float heightScoreThreshold1;
    float heightScoreThreshold2;
    float dilationPercentage;
    float whitespaceThresholdFactor;

    public NoiseReducer() {

    }

    public NoiseReducer(Image _img, float _proximityThreshold1, float _proximityThreshold2, float _heightThreshold1,
            float _heightThreshold2, float _dilationPercentage, float _whitespaceThresholdFactor) {
        img = _img;
        img = removeBorderWhiteSpace();
        borderProximityThreshold1 = _proximityThreshold1;
        borderProximityThreshold2 = _proximityThreshold2;
        heightScoreThreshold1 = _heightThreshold1;
        heightScoreThreshold2 = _heightThreshold2;
        dilationPercentage = _dilationPercentage;
        whitespaceThresholdFactor = _whitespaceThresholdFactor;
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

    public Image getCleanImage2() {
        getTextComponents2(segmentation.getComponents(),
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
        // Component[] filteredComponents4 = checkBorderProximity2(filteredComponents2,
        // segmentation.getComponentsCount());
        // segmentation.setComponentsArray(filteredComponents4);

        // return filteredComponents3;
    }

    public void getTextComponents2(Component[] comps, int componentsCount) {
        // Border Proximity Check
        Component[] filteredComponents1 = checkBorderProximity2(segmentation.getComponents(),
                segmentation.getComponentsCount());
        segmentation.setComponentsArray(filteredComponents1);

        // Height
        Component[] filteredComponents2 = checkHeightScore(filteredComponents1, segmentation.getComponentsCount());
        segmentation.setComponentsArray(filteredComponents2);

        // Width
        Component[] filteredComponents3 = checkWidthScore(filteredComponents2, segmentation.getComponentsCount());
        segmentation.setComponentsArray(filteredComponents3);

        // Second Pass Border Noise Removal
        // Component[] filteredComponents4 = checkBorderProximity2(filteredComponents2,
        // segmentation.getComponentsCount());
        // segmentation.setComponentsArray(filteredComponents4);

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

    public void filterBorderWhitespace(RectangularBound<Integer> bound, int[] hp, int[] vp) {
        int horizantalWhitespaceThreshold = (int) (whitespaceThresholdFactor * hp.length);
        int hfi = 0;
        while (hfi < hp.length && hp[hfi] <= horizantalWhitespaceThreshold) // find the first value which is not a
                                                                            // whitespace
            hfi++;
        int hli = hp.length - 1;
        while (hli >= 0 && hp[hli] <= horizantalWhitespaceThreshold) // find the last value which is not a whitespace
            hli--;

        int verticalWhitespaceThreshold = (int) (whitespaceThresholdFactor * vp.length);
        int vfi = 0;
        while (vfi < vp.length && vp[vfi] <= verticalWhitespaceThreshold) // find the first value which is not a
                                                                          // whitespace
            vfi++;
        int vli = vp.length - 1;
        while (vli >= 0 && vp[vli] <= verticalWhitespaceThreshold) // find the last value which is not a whitespace
            vli--;

        if (vfi > vli || hfi > hli) {// most likely to be noise inside the bounds i.e. less than whitespace threshold
                                     // everywhere
            bound.invalidate();
            return;
        }

        bound.maxX = bound.minX + vli;
        bound.maxY = bound.minY + hli;

        bound.minX += vfi;
        bound.minY += hfi;
    }

    public Image removeBorderWhiteSpace() {
        RectangularBound<Integer> recBound = new RectangularBound<>();
        recBound.minX = 0;
        recBound.minY = 0;
        recBound.maxX = img.getWidth();
        recBound.maxY = img.getHeight();
        ProjectionProfile pp = new ProjectionProfile(img);
        int[] hp = pp.getHorizantalProfile();
        int[] vp = pp.getVerticalProfile();
        filterBorderWhitespace(recBound, hp, vp);
        // System.out.println(recBound.minX + " " + recBound.minY + " " + recBound.maxX
        // + " " + recBound.maxY);
        // System.out.println(hp[1100] + " " + vp[1100]);
        Image croppedImage = new Image(recBound.maxX - recBound.minX + 1, recBound.maxY - recBound.minY + 1,
                Image.TYPE.BIN);
        for (int i = 0; i < croppedImage.getWidth(); i++) {
            for (int j = 0; j < croppedImage.getHeight(); j++) {
                croppedImage.pixel[i][j] = img.pixel[i + recBound.minX][j + recBound.minY];
            }
        }
        // img = croppedImage;
        return croppedImage;
    }

    public int[] getAverageDimensions(Image _image) {
        if (_image.type != Image.TYPE.BIN) {
            _image = Binarization.simpleThreshold(_image, 128);
        }
        int[] widthHeight = new int[2];
        Segmentation segmentation = new Segmentation(_image);
        segmentation.segment();
        Component[] comps = segmentation.getComponents();
        int componentsCount = segmentation.getComponentsCount();

        int widhtSum = 0;
        int heightSum = 0;
        for (int i = 0; i < componentsCount; i++) {
            widhtSum += comps[i].getRect()[2];
            heightSum += comps[i].getRect()[3];
        }
        widthHeight[0] = widhtSum / componentsCount;
        widthHeight[1] = heightSum / componentsCount;
        return widthHeight;
    }

    public void test() {
        int[] wh = getAverageDimensions(ImageUtility.readImage("test.png"));
        System.out.println(wh[0] + " " + wh[1]);
    }

    public Component[] checkHeightScorePX(Component[] comps, int componentsCount, int heightScoreThresholdPX) {
        Component[] newComps = new Component[componentsCount];
        int count = 0;

        for (int i = 0; i < componentsCount; i++) {
            int[] compRect = comps[i].getRect(); // minX,minY,width,height
            if ((compRect[3] < (heightScoreThresholdPX))) {
                newComps[count] = comps[i];
                count++;
            }
        }
        return newComps;
    }

    public Component[] checkWidthScorePX(Component[] comps, int componentsCount, int heightScoreThresholdPX) {
        Component[] newComps = new Component[componentsCount];
        int count = 0;

        for (int i = 0; i < componentsCount; i++) {
            int[] compRect = comps[i].getRect(); // minX,minY,width,height
            if ((compRect[2] < (heightScoreThresholdPX))) {
                newComps[count] = comps[i];
                count++;
            }
        }
        return newComps;
    }

}
