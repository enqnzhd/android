package io.lutics.app.android.util;

import android.content.Context;

import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Collection;

import io.lutics.app.android.BuildConfig;
import io.lutics.app.android.R;

/**
 * @author Sunny-J
 * @description Singleton for Google Analytics
 */
public class GoogleAnalyticsSingleton {

    // Variable
    private static GoogleAnalyticsSingleton mInstance;

    private static Context context;

    private Tracker tracker;

    // Constructor
    private GoogleAnalyticsSingleton(Context context) {
        this.context = context;
    }

    // Getter
    public static synchronized GoogleAnalyticsSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GoogleAnalyticsSingleton(context);
        }
        return mInstance;
    }

    public synchronized Tracker getTracker() {
        if (tracker == null) {
            GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(context.getApplicationContext());
            googleAnalytics.setDryRun(BuildConfig.DEBUG);

            tracker = googleAnalytics.newTracker(context.getResources().getString(R.string.app_analytics_id));
            tracker.enableExceptionReporting(true);
            tracker.enableAdvertisingIdCollection(true);

            ArrayList<String> packages = new ArrayList<>();
            packages.add(context.getPackageName());

            ExceptionReporter reporter = new ExceptionReporter(tracker, Thread.getDefaultUncaughtExceptionHandler(), context);
            reporter.setExceptionParser(new AnalyticsExceptionParser(context, packages));
            Thread.setDefaultUncaughtExceptionHandler(reporter);
        }
        return tracker;
    }

    /**
     * @author Sunny-J
     * @description Inner Class for Analytics
     */
    public class AnalyticsExceptionParser extends StandardExceptionParser {

        // Constructor
        public AnalyticsExceptionParser(Context context, Collection<String> additionalPackages) {
            super(context, additionalPackages);
        }

        @Override
        public String getDescription(String threadName, Throwable t) {
            return getDescription(getCause(t), getBestStackTraceElement(getCause(t)), threadName);
        }

        protected String getDescription(Throwable cause, StackTraceElement element, String threadName) {
            StringBuilder descriptionBuilder = new StringBuilder();
            descriptionBuilder.append(cause.getClass().getSimpleName());
            if (element != null) {
                descriptionBuilder.append(String.format(" (@%s:%s:%s)", element.getClassName(), element.getMethodName(), element.getLineNumber()));
            }

            if (threadName != null) {
                descriptionBuilder.append(String.format(" {%s}", threadName));
            }

            return descriptionBuilder.toString();
        }
    }
}
