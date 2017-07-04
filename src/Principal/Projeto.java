/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Guilherme
 */
package Principal;

import com.jsyn.data.FloatSample;
import com.sun.opengl.util.GLUT;
import java.util.ArrayList;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.media.opengl.GL.GL_AMBIENT_AND_DIFFUSE;
import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_FRONT_AND_BACK;
import static javax.media.opengl.GL.GL_NORMALIZE;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_GEN_S;
import static javax.media.opengl.GL.GL_TEXTURE_GEN_T;
import javax.media.opengl.glu.GLUquadric;

public class Projeto implements GLEventListener {

    // <editor-fold defaultstate="collapsed" desc="Declaracao">
    GLU glu = new GLU();
    GLUT glut = new GLUT();
    JSynPlayer player = new JSynPlayer();
    FloatSample sample;
    Texture theTexture;
    double frequencia;
    double periodo;
    double confidencia;
    double input;
    float luzAmbiente[] = {0f, 0f, 0f, 1f};
    float luzDifusa[] = {1f, 1f, 1f, 1f};
    float luzEspecular[] = {1f, 1f, 1f, 1f};
    float matDifusa[] = {1f, 0f, 0f, 0f};
    float especularidade[] = {1f, 1f, 1f, 1f};
    int especMaterial = 60;
    float[] rgba = {0.3f, 0.5f, 1f};
    Texture earthTexture;
    Texture fundo;    
    InputStream stream;
    TextureData data;
    int rot = 0;
    int rot2 = -90;
    int rot3 = 0;
    int timer = 0;
    float exp = 0;
    float cont = 0, cont2 = 0, cont3 = 0;
    int x = 0, y = 0, z = 300;
    int rott1 = 0;
    int rott2 = 0;
    int rott3 = 0;
    int rott4 = 0;
    int rott5 = 0;
    int rott6 = 0;
    int rott7 = 0;
    float sobe1 = 0;
    float sobe2 = 1;
    float sobe3 = 2;
    float sobe4 = 3;
    float sobe5 = 4;
    double inc1 = 0.05;
    double inc2 = 0.05;
    double inc3 = 0.049999;
    double inc4 = 0.05;
    double inc5 = 0.05;
    boolean first = true;
    int incx = 1, incy = 1;
    // </editor-fold>
    
    public static void main(String args[]) {
        new Projeto();
    }

    public Projeto() {
        GLJPanel canvas = new GLJPanel();
        canvas.addGLEventListener(this);

        player.addPlayList("nirvana.wav");
        player.init();
        player.play();

        JFrame frame = new JFrame("Projeto Gui");
        frame.setSize(700, 700);
        frame.getContentPane().add(canvas);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        System.exit(0);
                    }
                }).start();
            }
        });
    }

    public void init(GLAutoDrawable glAuto) {
        Animator ani = new Animator(glAuto);
        ani.start();
        GL gl = glAuto.getGL();
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL_NORMALIZE);

    }

    public void display(GLAutoDrawable glAuto) {
        try {
            // <editor-fold defaultstate="collapsed" desc="Inicia display">
            GL gl = glAuto.getGL();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            gl.glLoadIdentity();
            gl.glTranslated(0, 0, -10);
//        gl.glTranslated(0, 0, -(timer));
//        gl.glRotated(timer, 0, 0, 1);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Atualiza variaveis">

            timer++;
            frequencia = player.musica.frequency.getValue();
            periodo = player.musica.period.getValue();
            confidencia = player.musica.confidence.getValue();
            input = player.musica.input.getValue();
//            System.out.println("Freq: "+ frequencia);
//            System.out.println("periodo: "+ periodo);
//            System.out.println("confidencia: "+ confidencia);
//            System.out.println("input: "+ input);
            System.out.println("timer: " + timer);

        // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Iluminacao">
            if (timer < 1900) {
                atualizaposluz();
            }
            if (timer > 1900) {
                x = 0;
                y = 0;
            }

            float[] luzPos = {x, y, z, 0};
            // float[] luzPos        =   {0,    0,   100,  0   };
            gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, luzPos, 0);
            gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, luzAmbiente, 0);
            gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, luzDifusa, 0);
            gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, luzEspecular, 0);

            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, rgba, 0);
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, matDifusa, 0);
            gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, especularidade, 0);
            gl.glMateriali(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, especMaterial);

            gl.glEnable(GL.GL_LIGHTING);
            gl.glEnable(GL.GL_LIGHT1);
            gl.glEnable(GL.GL_COLOR_MATERIAL);
            gl.glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
      // </editor-fold>

            //DESENHA PRINCIPAL
            gl.glPushMatrix();
                gl.glColor3d(0, 0, 1);
                gl.glTranslated(0, 0, -80);

                if (timer > 2500 && rot2 < 0) {
                    gl.glRotated(rot2++, 1, 0, 0);
                }
                if (timer > 2500) {
                    gl.glRotated(90, 1, 0, 0);
                }
                if (timer < 2500) {
                    gl.glColor3d(confidencia * Math.random(), confidencia * Math.random(), confidencia * Math.random());
                } else {
                    gl.glColor3d(0.3, 0.6, 0.7);
                }
               
                gl.glPushMatrix();
                if (timer < 2500) {
                    gl.glScaled((float) frequencia * 0.01, (float) frequencia * 0.01, 1);
                } else if (timer < 3000) {
                    gl.glScaled(2, 2, (float) frequencia * 0.01);
                }
                if (timer < 3000) {
                    glut.glutSolidCylinder(1, 10, 16, 16);
                }
                gl.glPopMatrix();
                if (timer > 2900 && timer < 4000) {
                    if(timer>3200 && timer<4000)
                        gl.glRotated(frequencia, 0 , 1, 0);
                    estrela(gl);
                }
            gl.glPopMatrix();

            if (timer < 2800 || timer > 4000) {
                //DESENHA RISCOS
                gl.glPushMatrix();
                    gl.glTranslated(0, 0, -80);
                    riscos(gl);
                gl.glPopMatrix();

                gl.glPushMatrix();
                    gl.glTranslated(0, 0, -80);
                    gl.glRotated(180, 0, 1, 0);
                    riscos1(gl);
                gl.glPopMatrix();
            }

            //DESENHO EXPLOSAO
            if ((timer > 600 && timer < 1500)) {
                explosao(gl);
            }

            //DESENHO CUBOS SAINDO DA TELA GIRANDO
            if ((timer > 1000 && timer < 2000) || timer > 3800) {
                gl.glTranslated(0, 0, -80);
                cubosGirando(gl);
            }

            //ARVORE
            gl.glPushMatrix();
                //cor com freq
                gl.glTranslated(0, -6, -25);
                gl.glRotated(-50, 1, 0, 0);
                if (timer > 4000) {
                    arvore(gl);
                }
            gl.glPopMatrix();

            if (timer > 5000) {
                deathstar(gl);
            }

            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(Projeto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cubosGirando(GL gl)   {
         gl.glPushMatrix();
                gl.glRotated(rot++, 0, 0, 1);
                gl.glTranslated(2, 0, cont);
                glut.glutSolidCube(1);
                cont += 0.2;
                gl.glPopMatrix();

                gl.glPushMatrix();
                gl.glRotated(rot++, 0, 0, 1);
                gl.glTranslated(2, 0, cont2);
                glut.glutSolidCube(1);
                cont2 += 0.15;
                gl.glPopMatrix();

                gl.glPushMatrix();
                gl.glRotated(-(rot++), 0, 0, 1);
                gl.glTranslated(2, 0, cont3);
                glut.glutSolidCube(1);
                cont3 += 0.2;
                gl.glPopMatrix();
    }
    
    public void estrela(GL gl)  {
         gl.glPushMatrix();
                //gl.glScaled(2,2, (float)frequencia*0.01);
                gl.glScaled(2, 2, 3);
                glut.glutSolidCylinder(1, 10, 16, 16);
                gl.glPopMatrix();

                gl.glPushMatrix();

                gl.glRotated(rott1, 0, 1, 0);
                gl.glScaled(2, 2, 3);
                glut.glutSolidCylinder(1, 10, 16, 16);
                gl.glPopMatrix();

                gl.glPushMatrix();
                gl.glRotated(rott2, 0, 1, 0);
                gl.glScaled(2, 2, 3);
                glut.glutSolidCylinder(1, 10, 16, 16);
                gl.glPopMatrix();

                gl.glPushMatrix();

                gl.glRotated(rott3, 0, 1, 0);
                gl.glScaled(2, 2, 3);
                glut.glutSolidCylinder(1, 10, 16, 16);
                gl.glPopMatrix();

                gl.glPushMatrix();
                gl.glRotated(rott4, 0, 1, 0);
                gl.glScaled(2, 2, 3);
                glut.glutSolidCylinder(1, 10, 16, 16);
                gl.glPopMatrix();

                gl.glPushMatrix();
                gl.glRotated(rott5, 0, 1, 0);
                gl.glScaled(2, 2, 3);

                glut.glutSolidCylinder(1, 10, 16, 16);
                gl.glPopMatrix();
                gl.glPushMatrix();
                gl.glRotated(rott6, 0, 1, 0);
                gl.glScaled(2, 2, 3);

                glut.glutSolidCylinder(1, 10, 16, 16);
                gl.glPopMatrix();

                gl.glPushMatrix();
                gl.glRotated(rott7, 0, 1, 0);
                gl.glScaled(2, 2, 3);

                glut.glutSolidCylinder(1, 10, 16, 16);
                gl.glPopMatrix();

                if (rott1 > -45) {
                    rott1--;
                }

                if (rott2 > -90) {
                    rott2--;
                }

                if (rott3 > -135) {
                    rott3--;
                }

                if (rott4 > -180) {
                    rott4--;
                }

                if (rott5 > -225) {
                    rott5--;
                }

                if (rott6 > -270) {
                    rott6--;
                }

                if (rott7 > -315) {
                    rott7--;
                }
    }
    
    public void arvore(GL gl) {
        gl.glScaled(1, 20, 1);

        gl.glPushMatrix();
        gl.glColor3d(frequencia, 0, 0);
        gl.glTranslated(18, (sobe1 += inc1), 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glColor3d(frequencia, 0, 0);
        gl.glTranslated(-18, (sobe1 += inc1), 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glColor3d(0, frequencia, 0);
        gl.glTranslated(14, (sobe2 += inc2), 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glColor3d(Math.random(), frequencia, 0);
        gl.glTranslated(-14, (sobe2 += inc2), 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glColor3d(Math.random(), 0, Math.random());
        gl.glTranslated(10, (sobe3 += inc3), 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glColor3d(Math.random(), 0, Math.random());
        gl.glTranslated(-10, (sobe3 += inc3), 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glColor3d(0, Math.random(), Math.random());
        gl.glTranslated(6, (sobe4 += inc4), 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glColor3d(0, Math.random(), Math.random());
        gl.glTranslated(-6, (sobe4 += inc4), 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glColor3d(Math.random(), Math.random(), Math.random());
        gl.glTranslated(0, (sobe5 += inc5 * 2), 0);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        att();
    }

    public void att() {
        if (first) {
            sobe1 += inc1;
            if (sobe1 > 10) {
                inc1 *= -1;
            }

            sobe2 += inc2;
            if (sobe2 > 9) {
                inc2 *= -1;
            }

            sobe3 += inc3;
            if (sobe3 > 8) {
                inc3 *= -1;
            }

            sobe4 += inc4;
            if (sobe4 > 6) {
                inc4 *= -1;
            }

            sobe5 += inc5;
            if (sobe5 > 5) {
                inc5 *= -1;
            }

            if (inc1 < 1 && inc2 < 1 && inc3 < 1 && inc4 < 1 && inc5 < 1) {
                first = false;
            }
        }

        sobe1 += inc1;
        if (sobe1 > 10 || sobe1 < 0) {
            inc1 *= -1;
        }

        sobe2 += inc2;
        if (sobe2 > 10 || sobe2 < 0) {
            inc2 *= -1;
        }

        sobe3 += inc3;
        if (sobe3 > 10 || sobe3 < 0) {
            inc3 *= -1;
        }

        sobe4 += inc4;
        if (sobe4 > 10 || sobe4 < 0) {
            inc4 *= -1;
        }

        sobe5 += inc5;
        if (sobe5 > 10 || sobe5 < 0) {
            inc5 *= -1;
        }

    }

    public void atualizaposluz() {
        x += incx;
        y += incy;
        if (x > 500 || x < -500) {
            incx *= -1;
        }
        if (y > 500 || y < -500) {
            incy *= -1;
        }
    }

    public void explosao(GL gl) {

        gl.glPushMatrix();
        gl.glTranslated(0, 0, -20);

        for (int i = 0; i < 24; i++) {
            gl.glPushMatrix();
            gl.glColor3d(Math.random(), Math.random(), Math.random());
            gl.glRotated(15 * i, 0, 0, 1);
            gl.glTranslated(exp, 0, 0);
            glut.glutSolidCube(1);
            gl.glPopMatrix();
        }

        exp += 0.1;

        gl.glPopMatrix();
    }

    public void deathstar(GL gl) {
        try {
            stream = getClass().getResourceAsStream("logo2.jpg");
            data = TextureIO.newTextureData(stream, false, "jpg");
            earthTexture = TextureIO.newTexture(data);
        } catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }
        //DESENHA DEATH STAR
        gl.glPushMatrix();
        gl.glColor3d(1, 1, 1);

        gl.glRotated(90, 1, 0, 0);
        gl.glRotated(180, 0, 0, 1);
        //gl.glRotated(rot++, 0, 0, 1);
        // Apply texture.
        earthTexture.enable();
        earthTexture.bind();
        // Draw sphere (possible styles: FILL, LINE, POINT).
        GLUquadric earth = glu.gluNewQuadric();
        glu.gluQuadricTexture(earth, true);
        glu.gluQuadricDrawStyle(earth, GLU.GLU_FILL);
        glu.gluQuadricNormals(earth, GLU.GLU_FLAT);
        glu.gluQuadricOrientation(earth, GLU.GLU_OUTSIDE);

        glu.gluSphere(earth, (float) frequencia * 0.03, 16, 16);
        glu.gluDeleteQuadric(earth);
        gl.glPopMatrix();

        //FIM DESENHA DEATH STAR
    }

    public void riscos1(GL gl) {
        gl.glPushMatrix();
        gl.glColor3d(0, 0, 1);
        gl.glRotated(25, 0, -1, 1);
        gl.glTranslated((float) periodo / 10 * sin(20) / 2, 0, 0);
        gl.glScaled((float) periodo / 10 * sin(20), cos(20) * 10, 5);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();

        gl.glColor3d(1, 0, 0);
        gl.glRotated(45, 0, -1, 1);
        gl.glTranslated((float) periodo / 8 * sin(20) / 2, 0, 0);
        gl.glScaled((float) periodo / 8 * sin(20), cos(20) * 10, 5);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();

        gl.glColor3d(0, 1, 0);
        gl.glRotated(60, 0, -1, 1);
        gl.glTranslated((float) periodo / 6 * sin(20) / 2, 0, 0);
        gl.glScaled((float) periodo / 6 * sin(20), cos(20) * 10, 5);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

    }

    public void riscos(GL gl) {
        gl.glPushMatrix();
        gl.glColor3d(1, 0, 0);
        gl.glRotated(25, 0, 1, 1);
        gl.glTranslated((float) periodo / 10 * sin(20) / 2, 0, 0);
        gl.glScaled((float) periodo / 10 * sin(20), cos(20) * 10, 5);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();

        gl.glColor3d(0, 1, 0);
        gl.glRotated(45, 0, 1, 1);
        gl.glTranslated((float) periodo / 8 * sin(20) / 2, 0, 0);
        gl.glScaled((float) periodo / 8 * sin(20), cos(20) * 10, 5);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPushMatrix();

        gl.glColor3d(0, 0, 1);
        gl.glRotated(60, 0, 1, 1);
        gl.glTranslated((float) periodo / 6 * sin(20) / 2, 0, 0);
        gl.glScaled((float) periodo / 6 * sin(20), cos(20) * 10, 5);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

    }

    public void reshape(GLAutoDrawable gLAutoDrawable, int x, int y, int w, int h) {

        GL gl = gLAutoDrawable.getGL();
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(60, 1, 1, 999);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslated(0, 0, -5);
    }

    public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
    }
//    }
//    
//    public void drawBackground( GL gl) 
//    {
//        gl.glColor3d(1, 1, 1);
//      gl.glMatrixMode(GL.GL_PROJECTION);
//      gl.glPushMatrix();
//      gl.glOrtho(0, 1, 0, 1, 0, 1);
//
//      gl.glMatrixMode(GL.GL_MODELVIEW);
//      gl.glPushMatrix();
//      gl.glLoadIdentity();
//
//      // No depth buffer writes for background.
//      gl.glDepthMask( false );
//
//      fundo.enable();
//      fundo.bind();
//      gl.glBegin( GL.GL_QUADS ); {
//        gl.glTexCoord2f( 0f, 0f );
//        gl.glVertex2f( 0, 0 );
//        gl.glTexCoord2f( 0f, 1f );
//        gl.glVertex2f( 0, 1f );
//        gl.glTexCoord2f( 1f, 1f );
//        gl.glVertex2f( 1f, 1f );
//        gl.glTexCoord2f( 1f, 0f );
//        gl.glVertex2f( 1f, 0 );
//      } gl.glEnd();
//
//      gl.glDepthMask( true );
//
//      gl.glPopMatrix();
//      gl.glMatrixMode(GL.GL_PROJECTION);
//      gl.glPopMatrix();
//      gl.glMatrixMode(GL.GL_MODELVIEW);
//    }

}
