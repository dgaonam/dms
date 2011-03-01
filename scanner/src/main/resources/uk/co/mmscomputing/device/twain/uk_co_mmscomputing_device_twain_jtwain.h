/* DO NOT EDIT THIS FILE - it is machine generated */
#include "jni.h"
/* Header for class uk_co_mmscomputing_device_twain_jtwain */

#ifndef _Included_uk_co_mmscomputing_device_twain_jtwain
#define _Included_uk_co_mmscomputing_device_twain_jtwain
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ninitLib
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ninitLib
  (JNIEnv *, jclass);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    nstart
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_nstart
  (JNIEnv *, jclass);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ngetPtrSize
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ngetPtrSize
  (JNIEnv *, jclass);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ntrigger
 * Signature: (Ljava/lang/Object;I)V
 */
JNIEXPORT void JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ntrigger
  (JNIEnv *, jclass, jobject, jint);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ncallSourceManager
 * Signature: (III[B)I
 */
JNIEXPORT jint JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ncallSourceManager
  (JNIEnv *, jclass, jint, jint, jint, jbyteArray);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ngetContainer
 * Signature: ([B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ngetContainer
  (JNIEnv *, jclass, jbyteArray);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    nsetContainer
 * Signature: ([B[B)V
 */
JNIEXPORT void JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_nsetContainer
  (JNIEnv *, jclass, jbyteArray, jbyteArray);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    nfreeContainer
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_nfreeContainer
  (JNIEnv *, jclass, jbyteArray);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ncallSource
 * Signature: ([BIII[B)I
 */
JNIEXPORT jint JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ncallSource
  (JNIEnv *, jclass, jbyteArray, jint, jint, jint, jbyteArray);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ngetCallBackMethod
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ngetCallBackMethod
  (JNIEnv *, jclass);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ntransferImage
 * Signature: (J)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ntransferImage
  (JNIEnv *, jclass, jlong);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    nnew
 * Signature: ([BI)V
 */
JNIEXPORT void JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_nnew
  (JNIEnv *, jclass, jbyteArray, jint);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ncopy
 * Signature: ([B[BI)I
 */
JNIEXPORT jint JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ncopy
  (JNIEnv *, jclass, jbyteArray, jbyteArray, jint);

/*
 * Class:     uk_co_mmscomputing_device_twain_jtwain
 * Method:    ndelete
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_uk_co_mmscomputing_device_twain_jtwain_ndelete
  (JNIEnv *, jclass, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif
