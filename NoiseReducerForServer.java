public class NoiseReducerForServer {
    public static void main(String args[]) {
        reduceNoiseAndSave(args[0], args[1]);
//         reduceNoiseAndSave("bin_img (15).png", "denoised_img_15.png");
    }

    public static void reduceNoiseAndSave(String imagepath, String savepath) {
        Image srcimg = ImageUtility.readImageInGrayScale(imagepath);
        // convert to binarized form
        Image binarizedImage = Binarization.simpleThreshold(srcimg, 128);

        NoiseReducer noiseRed = new NoiseReducer(binarizedImage, 0.005f, 0.015f, 0.2f, 0.1f, 0.05f, 0.005f);
        Image denoisedImage = noiseRed.getCleanImage();
        // noiseRed.removeProbableImages(denoisedImage2);

        NoiseReducer noiseRed2 = new NoiseReducer(denoisedImage, 0.005f, 0.015f, 0.2f, 0.1f, 0.05f, 0.005f);
        Image denoisedImage2 = noiseRed2.getCleanImage2();

        // save binarized image
        ImageUtility.writeImage(denoisedImage2, savepath);
    }
}
