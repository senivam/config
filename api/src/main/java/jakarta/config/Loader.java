/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jakarta.config;

import java.util.ServiceLoader;

/**
 * A loader of configuration-related objects.
 *
 * <p>The loader <em>resolves</em> configuration data of the provided <em>configuration interface</em> with a portion of
 * application's <em>persistent configuration</em> identified by <em>configuration path</em>. The portion of the
 * <em>persistent configuration</em> is identified by <em>configuration path</em>.</p>
 *
 * <p>The loader must support <em>persistent configuration</em> stored in <em>META-INF/jakarta-config.properties</em>
 * file found on the classpath that follows a format that is recognized by the class {@link java.util.Properties}.</p>
 *
 * <p>In the following example the {@code MyConfigurationRelatedObject} is the <em>configuration interface</em> to be
 * <em>resolved</em>. An instance of the <em>configuration interface</em> is created by the {@link Loader}:</p>
 *
 * <blockquote><pre> {@linkplain Loader Loader} loader = {@linkplain Loader Loader}.{@linkplain Loader#bootstrap() bootstrap()};
 *MyConfigurationRelatedObject object = null;
 *try {
 *  object = loader
 *             .{@linkplain #path(String) path("my.configuration")}
 *             .{@linkplain #load(Class) load(MyConfigurationRelatedObject.class)};
 *} catch ({@linkplain NoSuchObjectException} noSuchObjectException) {
 *  // object is <a href="doc-files/terminology.html#absent">absent</a>
 *} catch ({@linkplain ConfigException} configException) {
 *  // a {@linkplain #load(Class) loading}-related error occurred
 *}</pre></blockquote>
 *
 * <p>Implementations of the methods in this class must be:</p>
 * <ul>
 *     <li>idempotent</li>
 *     <li>safe for concurrent use by multiple threads</li>
 *     <li>must not return {@code null}.</il>
 * </ul>
 *
 * @see #bootstrap()
 *
 * @see #bootstrap(ClassLoader)
 *
 * @see #load(Class)
 *
 * @see #load(TypeToken)
 *
 * @see #path(String)
 *
 * @see <a href="doc-files/terminology.html">Terminology</a>
 */
public interface Loader {

    /**
     * Loads a configuration-related object of the supplied {@code
     * type} and returns it.
     *
     * <p><strong>Note:</strong> The rules governing how it is
     * determined whether any given configuration-related object is
     * "of the supplied {@code type}" are currently wholly
     * undefined.</p>    
     *
     * <p>Implementations of this method may or may not return a <a
     * href="doc-files/terminology.html#determinate">determinate</a>
     * value.</p>
     *
     * @param <T> the type of object to load
     *
     * @param type the type of object to load; must not be {@code
     * null}
     *
     * @return the loaded object; never {@code null}
     *
     * @exception NoSuchObjectException if the invocation was sound
     * but the requested object was <a
     * href="doc-files/terminology.html#absent">absent</a>
     *
     * @exception ConfigException if the invocation was sound but the
     * object could not be loaded for any reason not related to <a
     * href="doc-files/terminology.html#absent">absence</a>
     *
     * @exception IllegalArgumentException if the suplied {@code type}
     * was invalid for any reason
     *
     * @exception NullPointerException if the supplied {@code type}
     * was {@code null}
     */
    public <T> T load(Class<T> type);

    /**
     * Loads a configuration-related object of the supplied {@code
     * type} and returns it.
     *
     * <p><strong>Note:</strong> The rules governing how it is
     * determined whether any given configuration-related object is
     * "of the supplied {@code type}" are currently wholly
     * undefined.</p>
     *
     * <p>Implementations of this method may or may not return a <a
     * href="doc-files/terminology.html#determinate">determinate</a>
     * value.</p>
     *
     * @param <T> the type of object to load
     *
     * @param type the type of object to load; must not be {@code null}
     *
     * @return the loaded object; never {@code null}
     *
     * @exception NoSuchObjectException if the invocation was sound
     * but the requested object was <a
     * href="doc-files/terminology.html#absent">absent</a>
     *
     * @exception ConfigException if the invocation was sound but the
     * object could not be loaded for any reason not related to <a
     * href="doc-files/terminology.html#absent">absence</a>
     *
     * @exception IllegalArgumentException if the suplied {@code type}
     * was invalid for any reason
     *
     * @exception NullPointerException if the supplied {@code type}
     * was {@code null}
     */
    public <T> T load(TypeToken<T> type);

    /**
     * Return a new instance of a {@link Loader} with the <em>configuration path</em> set.
     *
     * The <em>configuration path</em> identifies where the configuration relevant for the annotated configuration class is found
     * in a given application's <em>persistent configuration</em>.
     *
     * <p>The configuration path uses the dot symbol as a separator.</p>
     *
     * <p>For instance, if the <em>persistent configuration</em> contains
     * <pre>  my.configuration.user=tester</pre>
     * the <em>configuration path</em> for the configuration portion {@code user=tester} would be {@code my.configuration}.
     * </p>
     *
     * @param path a configuration path.
     * @return a new instance of the {@link Loader} class with a configured <em>path</em>.
     *
     * @see Configuration#path() Configuration#path
     */
    public Loader path(String path);

    /**
     * <em>{@linkplain #bootstrap(ClassLoader) Bootstraps}</em> a
     * {@link Loader} instance for subsequent usage using the
     * {@linkplain Thread#getContextClassLoader() context
     * classloader}.
     *
     * <p>This method never returns {@code null}.</p>
     *
     * <p>This method is idempotent.</p>
     *
     * <p>This method is safe for concurrent use by multiple
     * threads.</p>
     *
     * <p>This method may or may not return a <a
     * href="doc-files/terminology.html#determinate">determinate</a>
     * value. See {@link #bootstrap(ClassLoader)} for details.</p>
     *
     * <p>Except as possibly noted above, the observable behavior of
     * this method is specified to be identical to that of the {@link
     * #bootstrap(ClassLoader)} method.</p>
     *
     * @return a {@link Loader}; never {@code null}
     *
     * @exception java.util.ServiceConfigurationError if bootstrapping
     * failed because of a {@link ServiceLoader#load(Class,
     * ClassLoader)} or {@link ServiceLoader#findFirst()} problem
     *
     * @exception ConfigException if bootstrapping failed because of a
     * {@link Loader#load(Class)} problem
     *
     * @see #bootstrap(ClassLoader)
     */
    public static Loader bootstrap() {
        return bootstrap(Thread.currentThread().getContextClassLoader());
    }

    /**
     * <em>Bootstraps</em> a {@link Loader} instance for subsequent
     * usage.
     *
     * <p>The bootstrap process proceeds as follows:</p>
     *
     * <ol>
     *
     * <li>A <em>primordial {@link Loader}</em> is located with
     * observable effects equal to those resulting from executing the
     * following code:
     *
     * <blockquote><pre>{@linkplain Loader} loader = {@linkplain ServiceLoader}.{@linkplain ServiceLoader#load(Class, ClassLoader) load(Loader.class, classLoader)}
     *  .{@linkplain java.util.ServiceLoader#findFirst() findFirst()}
     *  .{@linkplain java.util.Optional#orElseThrow() orElseThrow}({@linkplain NoSuchObjectException#NoSuchObjectException() NoSuchObjectException::new});</pre></blockquote></li>
     *
     * <li>The {@link #load(Class)} method is invoked on the resulting
     * {@link Loader} with {@link Loader Loader.class} as its sole
     * argument.
     *
     * <ul>
     *
     * <li>If the invocation throws a {@link NoSuchObjectException},
     * the primordial {@link Loader} is returned.</li>
     *
     * <li>If the invocation returns a {@link Loader}, that {@link
     * Loader} is returned.</li>
     *
     * </ul>
     *
     * </li>
     *
     * </ol>
     *
     * <p>This method may or may not return a <a
     * href="doc-files/terminology.html#determinate">determinate</a>
     * value depending on the implementation of the {@link Loader}
     * loaded in step 2 above.</p>
     *
     * <p><strong>Note:</strong> The implementation of this method may
     * change without notice between any two versions of this
     * specification.  The requirements described above, however, will
     * be honored in any minor version of this specification within a
     * given major version.</p>
     *
     * @param classLoader the {@link ClassLoader} used to {@linkplain
     * ServiceLoader#load(Class, ClassLoader) locate service provider
     * files}; may be {@code null} to indicate the system classloader
     * (or bootstrap class loader) in accordance with the contract of
     * the {@link ServiceLoader#load(Class, ClassLoader)} method;
     * often is the return value of an invocation of {@link
     * Thread#getContextClassLoader()
     * Thread.currentThread().getContextClassLoader()}
     *
     * @return a {@link Loader}; never {@code null}
     *
     * @exception java.util.ServiceConfigurationError if bootstrapping
     * failed because of a {@link ServiceLoader#load(Class,
     * ClassLoader)} or {@link ServiceLoader#findFirst()} problem
     *
     * @exception ConfigException if bootstrapping failed because of a
     * {@link Loader#load(Class)} problem
     */
    public static Loader bootstrap(ClassLoader classLoader) {
        Loader loader = ServiceLoader.load(Loader.class, classLoader)
            .findFirst()
            .orElseThrow(NoSuchObjectException::new);
        try {
            return loader.load(Loader.class);
        } catch (NoSuchObjectException absentValueException) {
            System.getLogger(Loader.class.getName())
                .log(System.Logger.Level.DEBUG, absentValueException::getMessage, absentValueException);
            return loader;
        }
    }

}
