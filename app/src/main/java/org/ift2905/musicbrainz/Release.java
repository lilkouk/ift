package org.ift2905.musicbrainz;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ift2905.musicbrainz.service.musicbrainz.Artist;
import org.ift2905.musicbrainz.service.musicbrainz.MusicBrainzService;
import org.ift2905.musicbrainz.service.musicbrainz.Recording;
import org.ift2905.musicbrainz.service.musicbrainz.ReleaseGroup;

import java.io.IOException;
import java.util.List;

public class Release extends AppCompatActivity {
    public TextView track;
    public  TextView artiste;
    public  TextView album;

    public TextView duree;
    public TextView numero;
    public ListView listView;
    public ImageView imageView;
    public Intent intent;
    public  ReleaseGroup releaseGroup;
    public MusicBrainzService service;
    public org.ift2905.musicbrainz.service.musicbrainz.Release release;
    public List<org.ift2905.musicbrainz.service.musicbrainz.Release> releases;
    public List<Recording> recordings;
    public List<Artist> artists;
    public Artist nonArtiste;

    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release1);


        //release = releases.get(0);

        numero = (TextView) findViewById(R.id.numero);
        track = (TextView) findViewById(R.id.track);
        artiste=(TextView) findViewById(R.id.artiste);
        album=(TextView) findViewById(R.id.album);
        duree = (TextView) findViewById(R.id.duree);
        listView = (ListView) findViewById(R.id.listView);
        imageView = (ImageView) findViewById(R.id.imageView);
        assert imageView != null;
        imageView.getDrawable();
        track.setText("Track");
        duree.setText("           Duree");
        numero.setText("N°");
        intent = getIntent();
        releaseGroup = (ReleaseGroup) intent.getSerializableExtra("releaseGroup");
        artists= releaseGroup.credits;
        if (artists!=null) {
            nonArtiste=artists.get(0);
            artiste.setText(nonArtiste.name);

        }
        RunAPI run = new RunAPI();
        run.execute();
        Picasso p = Picasso.with(getApplicationContext());
        p.setIndicatorsEnabled(true);

        //Picasso.with(getApplicationContext()).load(String.format("http://coverartarchive.org/release-group/%s/front", releaseGroup.id)).into(imageView);
        if (releaseGroup != null) {
            if (releaseGroup.id != null) {
                p.setIndicatorsEnabled(true);
                p.load(String.format("http://coverartarchive.org/release-group/%s/front", releaseGroup.id))
                        .placeholder(R.drawable.release_group_placeholder)
                        .into(imageView);
            }
        }

        //Picasso.with(getApplicationContext()).load(String.format("http://coverartarchive.org/release-group/%s/front", releaseGroup.id)).placeholder(R.drawable.release_group_placeholder).into(imageView);}
        else
            Picasso.with(getApplicationContext()).load("https://backseatmafia.files.wordpress.com/2014/09/supertramp-4ff0a439422bd-e1375570320343.jpg").into(imageView);

        /*Picasso p = Picasso.with(getApplicationContext());
        p.setIndicatorsEnabled(true);
        p.load(String.format("http://coverartarchive.org/release-group/%s/front", releaseGroup.id)).into(imageView);*/
        // Picasso.with(getApplicationContext()).load(String.format("http://coverartarchive.org/release-group/%s/front", releaseGroup.id)).into(imageView);

        //adapter=new MyAdapter();

    }

    public  class  RunAPI extends AsyncTask<String,Object, org.ift2905.musicbrainz.service.musicbrainz.Release>{
        @Override
        protected void onPostExecute(org.ift2905.musicbrainz.service.musicbrainz.Release release) {
            super.onPostExecute(release);
            adapter=new MyAdapter();
            listView.setAdapter(adapter);


        }

        @Override
        protected org.ift2905.musicbrainz.service.musicbrainz.Release doInBackground(String... params) {
            intent= getIntent();


            service =  new MusicBrainzService();
            releases = null;

            try {
                releases = (List<org.ift2905.musicbrainz.service.musicbrainz.Release>) service.getReleases(releaseGroup);

            } catch (IOException e) {
                e.printStackTrace();
            }
            release=releases.get(0);

            recordings= release.recordings;

            return release;
        }
    }

    public class MyAdapter extends BaseAdapter {
        LayoutInflater inflateur;

        public  MyAdapter(){
            inflateur =(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);



        }

        @Override
        public int getCount() {
            return recordings.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v= convertView;
            if(v==null){
                v=inflateur.inflate(R.layout.vue,parent,false);

            }  album.setText(" Album : "+release.name);


            int duree= recordings.get(position).length;
            int minute= duree/60000;
            int seconde= duree % 10000;
            String k= ((Integer)seconde).toString();
            if (k.length()==0){
                k="00";


            }else if (k.length()==1)
                     k=k+"0";
            if(position%2==0) v.setBackgroundColor(Color.WHITE);
            else
                v.setBackgroundColor(Color.LTGRAY);
            TextView titre =(TextView) v.findViewById(R.id.titre);
            TextView  temps= (TextView) v.findViewById(R.id.temps);
            if (release!=null){
            titre.setText(((Integer)(position+1)).toString() +"           "+ recordings.get(position).name );}
            else
            titre.setText( " track" + ((Integer)position).toString());
            temps.setText(((Integer)(minute)).toString() + ":"+ k.substring(0,2));
            return v;

        }

    }
}

