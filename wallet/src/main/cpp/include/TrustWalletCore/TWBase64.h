// Copyright © 2017-2020 Trust Wallet.
//
// This file is part of Trust. The full Trust copyright notice, including
// terms governing use, modification, and redistribution, is contained in the
// file LICENSE at the root of the source code distribution tree.

#pragma once

#include "TWBase.h"
#include "TWData.h"
#include "TWString.h"

TW_EXTERN_C_BEGIN

TW_EXPORT_STRUCT
struct TWBase64 {
    uint8_t unused; // C doesn't allow zero-sized struct
};

/// Encodes data as a Base58 string, including the checksum.
TW_EXPORT_STATIC_METHOD
TWString *_Nonnull TWBase64Encode(TWData *_Nonnull data);

/// Encodes data as a Base58 string, not including the checksum.
TW_EXPORT_STATIC_METHOD
TWString *_Nonnull TWBase64EncodeUrl(TWData *_Nonnull data);

/// Decodes a Base58 string checking the checksum.
TW_EXPORT_STATIC_METHOD
TWData *_Nullable TWBase64Decode(TWString *_Nonnull string);

/// Decodes a Base58 string with no checksum.
TW_EXPORT_STATIC_METHOD
TWData *_Nullable TWBase64DecodeUrl(TWString *_Nonnull string);

TW_EXTERN_C_END
