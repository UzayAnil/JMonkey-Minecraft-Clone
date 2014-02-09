
package PerlinNoise;

import java.util.Random;

/**
 * This class helps with the generation of the world.
 * The forumula allows for hills and valleys in the game.
 * @author Lucas's Computer
 */
public class PerlinNoise 
{

    private static final int widthX = 1600;
    private static final int widthZ = 1600;
    
    /**
     * Generates a 2D array of WhiteNoise values
     * @param baseNoise
     * @param octaveCount
     * @return 2D array of float types representing WhiteNoise values
     */
    public static float[][] generateWhiteNoise()
    {
        Random random = new Random(0);
        float[][] noise = new float[widthX][widthZ];
        
        for (int i = 0; i < widthX; i++)
        {
            for (int j = 0; j < widthZ; j++)
            {
                noise[i][j] = (float)random.nextDouble() % 1;
            }
        }
        return noise;
    }
    
    /**
     * Generates a 2D array of SmoothNoise values, used for PerlinNoise
     * @param baseNoise
     * @param octaveCount
     * @return 2D array of float types representing SmoothNoise values
     */
    public static float[][] generateSmoothNoise(float[][] baseNoise, int octave)
    {
        int width = baseNoise.length;
        int height = baseNoise[0].length;

        float[][] smoothNoise = new float[width][height];

        int samplePeriod = 1 << octave; // calculates 2 ^ k
        float sampleFrequency = 1.0f / samplePeriod;
        
        for (int i = 0; i < width; i++)
        {
          //calculate the horizontal sampling indices
          int sample_i0 = (i / samplePeriod) * samplePeriod;
          int sample_i1 = (sample_i0 + samplePeriod) % width; //wrap around
          float horizontal_blend = (i - sample_i0) * sampleFrequency;

          for (int j = 0; j < height; j++)
          {
             //calculate the vertical sampling indices
             int sample_j0 = (j / samplePeriod) * samplePeriod;
             int sample_j1 = (sample_j0 + samplePeriod) % height; //wrap around
             float vertical_blend = (j - sample_j0) * sampleFrequency;

             //blend the top two corners
             float top = Interpolate(baseNoise[sample_i0][sample_j0],
                baseNoise[sample_i1][sample_j0], horizontal_blend);

             //blend the bottom two corners
             float bottom = Interpolate(baseNoise[sample_i0][sample_j1],
                baseNoise[sample_i1][sample_j1], horizontal_blend);

             //final blend
             smoothNoise[i][j] = Interpolate(top, bottom, vertical_blend);
          }
        }
        return smoothNoise;
    }
    
    private static float Interpolate(float x0, float x1, float alpha)
    {
        return x0 * (1 - alpha) + alpha * x1;
    }
    
    /**
     * Generates a 2D array of PerlinNoise values
     * @param baseNoise
     * @param octaveCount
     * @return 2D array of float types representing PerlinNoise values
     */
    public static float[][] generatePerlinNoise(float[][] baseNoise, int octaveCount)
    {
       int width = baseNoise.length;
       int height = baseNoise[0].length;

       float[][][] smoothNoise = new float[octaveCount][][]; //an array of 2D arrays containing

       float persistance = 0.3f;

       //generate smooth noise
       for (int i = 0; i < octaveCount; i++)
       {
           smoothNoise[i] = generateSmoothNoise(baseNoise, i);
       }

        float[][] perlinNoise = new float[width][height];
        float amplitude = 1.0f;
        float totalAmplitude = 0.0f;

        //blend noise together
        for (int octave = octaveCount - 1; octave >= 0; octave--)
        {
           amplitude *= persistance;
           totalAmplitude += amplitude;

           for (int i = 0; i < width; i++)
           {
              for (int j = 0; j < height; j++)
              {
                 perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
              }
           }
        }

       //normalisation
       for (int i = 0; i < width; i++)
       {
          for (int j = 0; j < height; j++)
          {
             perlinNoise[i][j] /= totalAmplitude;
          }
       }

       return perlinNoise;
    }
}
