package io.lutics.app.android.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.LinkedHashMap;

public class SeparatedListAdapter extends BaseAdapter {

    // Variables
    private final static int TYPE_SECTION_HEADER = 0;

    private ArrayAdapter<String> headers;
    private LinkedHashMap<String, Adapter> sections = new LinkedHashMap<>();

    // Constructor
    public SeparatedListAdapter(Context context, int layout) {
        headers = new ArrayAdapter<>(context, layout);
    }

    public void addSection(String section, Adapter adapter) {
        headers.add(section);
        sections.put(section, adapter);
    }

    @Override
    public int getCount() {
        int total = 0;
        for (Adapter adapter : this.sections.values())
            total += adapter.getCount() + 1;

        return total;
    }

    @Override
    public Object getItem(int position) {
        for (Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            if (position == 0)
                return section;
            if (position < size)
                return adapter.getItem(position - 1);

            position -= size;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int sectionNum = 0;
        for (Object section : this.sections.keySet()) {
            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;

            if (position == 0)
                return headers.getView(sectionNum, convertView, parent);
            if (position < size)
                return adapter.getView(position - 1, convertView, parent);

            position -= size;
            sectionNum++;
        }
        return null;
    }

    public int getViewTypeCount() {
        int total = 1;
        for (Adapter adapter : this.sections.values())
            total += adapter.getViewTypeCount();

        return total;
    }

    public int getItemViewType(int position) {
        int type = 1;
        for (Object section : this.sections.keySet()) {

            Adapter adapter = sections.get(section);
            int size = adapter.getCount() + 1; // +1 : header

            if (position == 0)
                return TYPE_SECTION_HEADER;
            if (position < size)
                return type + adapter.getItemViewType(position - 1);

            position -= size;
            type += adapter.getViewTypeCount();
        }
        return -1;
    }

    public boolean isEnabled(int position) {
        return (getItemViewType(position) != TYPE_SECTION_HEADER);
    }
}