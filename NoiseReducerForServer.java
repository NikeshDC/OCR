public class NoiseReducerForServer {
    public static void main(String args[]) {
        reduceNoiseAndSave(args[0], args[1]);
//         reduceNoiseAndSave("tempb.jpg", "denoised_tempb.png");
    }

    public static void reduceNoiseAndSave(String imagepath, String savepath) {
        Image srcimg = ImageUtility.readImageInGrayScale(imagepath);
        // convert to binarized form
        Image binarizedImage = Binarization.simpleThreshold(srcimg, 128);

        NoiseReducer noiseRed = new NoiseReducer(binarizedImage, 0.005f, 0.02f, 0.2f, 0.1f, 0.05f);
        Image denoisedImage2 = noiseRed.getCleanImage();

        // noiseRed.removeProbableImages(denoisedImage2);

        // save binarized image
        ImageUtility.writeImage(denoisedImage2, savepath);
    }
}
