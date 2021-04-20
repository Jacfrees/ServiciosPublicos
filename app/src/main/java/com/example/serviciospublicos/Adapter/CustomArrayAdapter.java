package com.example.serviciospublicos.Adapter;


import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.ArrayRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.RequiresApi;

import java.util.*;

@RequiresApi(api = Build.VERSION_CODES.M)
public class CustomArrayAdapter<T> extends BaseAdapter implements Filterable, ThemedSpinnerAdapter {


    private final Object mLock = new Object();

    private final LayoutInflater mInflater;

    private final Context mContext;

    private final int mResource;

    private int mDropDownResource;

    private List<T> mObjects;

    private boolean mObjectsFromResources;

    private int mFieldId = 0;

    private boolean mNotifyOnChange = true;

    // A copy of the original mObjects array, initialized from and then used instead as soon as
    // the mFilter ArrayFilter is used. mObjects will then only contain the filtered values.
    private ArrayList<T> mOriginalValues;
    private ArrayFilter mFilter;

    /**
     * Layout inflater used for {@link #getDropDownView(int, View, ViewGroup)}.
     */
    private LayoutInflater mDropDownInflater;

    public void setmFieldId(int mFieldId) {
        this.mFieldId = mFieldId;
    }

    public CustomArrayAdapter(@androidx.annotation.NonNull Context context, @LayoutRes int resource) {
        this(context, resource, 0, new ArrayList<T>());
    }

    public CustomArrayAdapter(@androidx.annotation.NonNull Context context, @LayoutRes int resource,
                        @IdRes int textViewResourceId) {
        this(context, resource, textViewResourceId, new ArrayList<T>());
    }


    public CustomArrayAdapter(@androidx.annotation.NonNull Context context, @LayoutRes int resource, @androidx.annotation.NonNull T[] objects) {
        this(context, resource, 0, Arrays.asList(objects));
    }

    public CustomArrayAdapter(@androidx.annotation.NonNull Context context, @LayoutRes int resource,
                        @IdRes int textViewResourceId, @androidx.annotation.NonNull T[] objects) {
        this(context, resource, textViewResourceId, Arrays.asList(objects));
    }

    public CustomArrayAdapter(@androidx.annotation.NonNull Context context, @LayoutRes int resource,
                        @androidx.annotation.NonNull List<T> objects) {
        this(context, resource, 0, objects);
    }

    public CustomArrayAdapter(@androidx.annotation.NonNull Context context, @LayoutRes int resource,
                              @IdRes int textViewResourceId, @androidx.annotation.NonNull List<T> objects) {
        this(context, resource, textViewResourceId, objects, false);
    }

    private CustomArrayAdapter(@androidx.annotation.NonNull Context context, @LayoutRes int resource,
                         @IdRes int textViewResourceId, @androidx.annotation.NonNull List<T> objects, boolean objsFromResources) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = mDropDownResource = resource;
        mObjects = objects;
        mObjectsFromResources = objsFromResources;
        mFieldId = textViewResourceId;
    }

    public void add(@androidx.annotation.Nullable T object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(object);
            } else {
                mObjects.add(object);
            }
            mObjectsFromResources = false;
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }


    public void addAll(@androidx.annotation.NonNull Collection<? extends T> collection) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.addAll(collection);
            } else {
                mObjects.addAll(collection);
            }
            mObjectsFromResources = false;
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void addAll(T ... items) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.addAll(mOriginalValues, items);
            } else {
                Collections.addAll(mObjects, items);
            }
            mObjectsFromResources = false;
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void insert(@androidx.annotation.Nullable T object, int index) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(index, object);
            } else {
                mObjects.add(index, object);
            }
            mObjectsFromResources = false;
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }


    public void remove( @androidx.annotation.Nullable T object) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.remove(object);
            } else {
                mObjects.remove(object);
            }
            mObjectsFromResources = false;
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                mOriginalValues.clear();
            } else {
                mObjects.clear();
            }
            mObjectsFromResources = false;
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void sort(@androidx.annotation.NonNull Comparator<? super T> comparator) {
        synchronized (mLock) {
            if (mOriginalValues != null) {
                Collections.sort(mOriginalValues, comparator);
            } else {
                Collections.sort(mObjects, comparator);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }


    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    public @androidx.annotation.NonNull Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public @androidx.annotation.Nullable
    T getItem(int position) {
        return mObjects.get(position);
    }

    public int getPosition( @androidx.annotation.Nullable T item) {
        return mObjects.indexOf(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public @androidx.annotation.NonNull
    View getView(int position, @androidx.annotation.Nullable View convertView,
                 @androidx.annotation.NonNull ViewGroup parent) {
        return createViewFromResource(mInflater, position, convertView, parent, mResource);
    }

    private @androidx.annotation.NonNull View createViewFromResource(@androidx.annotation.NonNull LayoutInflater inflater, int position,
                                                                     @androidx.annotation.Nullable View convertView, @androidx.annotation.NonNull ViewGroup parent, int resource) {
        final View view;
        final TextView text;

        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = view.findViewById(mFieldId);

                if (text == null) {
                    throw new RuntimeException("Failed to find view with ID "
                            + mContext.getResources().getResourceName(mFieldId)
                            + " in item layout");
                }
            }
        } catch (ClassCastException e) {
            Log.e("CustomArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "CustomArrayAdapter requires the resource ID to be a TextView", e);
        }

        final T item = getItem(position);
        if (item instanceof CharSequence) {
            text.setText((CharSequence) item);
        } else {
            text.setText(item.toString());
        }

        return view;
    }

    public void setDropDownViewResource(@LayoutRes int resource) {
        this.mDropDownResource = resource;
    }


    @Override
    public void setDropDownViewTheme( @androidx.annotation.Nullable Resources.Theme theme) {
        if (theme == null) {
            mDropDownInflater = null;
        } else if (theme == mInflater.getContext().getTheme()) {
            mDropDownInflater = mInflater;
        } else {
            final Context context = new ContextThemeWrapper(mContext, theme);
            mDropDownInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public  @androidx.annotation.Nullable Resources.Theme getDropDownViewTheme() {
        return mDropDownInflater == null ? null : mDropDownInflater.getContext().getTheme();
    }

    @Override
    public View getDropDownView(int position,  @androidx.annotation.Nullable View convertView,
                                @androidx.annotation.NonNull ViewGroup parent) {
        final LayoutInflater inflater = mDropDownInflater == null ? mInflater : mDropDownInflater;
        return createViewFromResource(inflater, position, convertView, parent, mDropDownResource);
    }


    public static @androidx.annotation.NonNull
    CustomArrayAdapter<CharSequence> createFromResource(@androidx.annotation.NonNull Context context,
                                                                       @ArrayRes int textArrayResId, @LayoutRes int textViewResId) {
        final CharSequence[] strings = context.getResources().getTextArray(textArrayResId);
        return new CustomArrayAdapter<>(context, textViewResId, 0, Arrays.asList(strings), true);
    }

    @Override
    public @androidx.annotation.NonNull
    Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        // First check if app developer explicitly set them.
        final CharSequence[] explicitOptions = super.getAutofillOptions();
        if (explicitOptions != null) {
            return explicitOptions;
        }

        // Otherwise, only return options that came from static resources.
        if (!mObjectsFromResources || mObjects == null || mObjects.isEmpty()) {
            return null;
        }
        final int size = mObjects.size();
        final CharSequence[] options = new CharSequence[size];
        mObjects.toArray(options);
        return options;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            final FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                final ArrayList<T> list;
                synchronized (mLock) {
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                final String prefixString = prefix.toString().toLowerCase();

                final ArrayList<T> values;
                synchronized (mLock) {
                    values = new ArrayList<>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<T> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final T value = values.get(i);
                    final String valueText = value.toString().toLowerCase();

                    // First match against the whole, non-splitted value
                    if (valueText.contains(prefixString)) {
                        newValues.add(value);
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<T>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
