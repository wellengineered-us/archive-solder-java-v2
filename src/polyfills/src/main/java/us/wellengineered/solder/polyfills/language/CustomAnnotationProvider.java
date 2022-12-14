/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.language;

import java.lang.annotation.Annotation;

public interface CustomAnnotationProvider
{
	<TAnnotation extends Annotation> TAnnotation[] getAnnotations(Class<? extends TAnnotation> annotationClass);
}
