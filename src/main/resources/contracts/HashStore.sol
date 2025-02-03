// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract HashStore {

    mapping(string => bool) private storedHash;  // Tracks if hash is already stored
    mapping(string => uint256) private hashToId; // Maps hash to unique ID
    uint256 private currentHashCount = 1;         // Keeps track of hash IDs

    string[] private hashesArray;
    event HashStored(uint256 indexed id, string hash, string message);

    // Struct for storing hash storage results
    struct StoreResult {
        string Hash;
        bool stored;
    }

    // Function to store hashes by taking input hashes as an array and returning array of objects
    function storeHash(string[] memory hashes) public returns (StoreResult[] memory) {
        uint256 length = hashes.length;
        StoreResult[] memory results = new StoreResult[](length);

        for (uint256 i = 0; i < length; i++) {
            string memory currentHash = hashes[i]; // store current hash being handled
            bool alreadyStored = false;

            if (storedHash[currentHash]) {
                alreadyStored = true; // The hash already exists in a batch
            }

            if (alreadyStored) {
                results[i] = StoreResult({
                    Hash: hashes[i],
                    stored: false // Already stored, do not store it again
                });
                continue; // Skip storing this hash again
            }


            hashesArray.push(currentHash);

            // Mark the hash as stored in the mapping
            storedHash[currentHash] = true;
            hashToId[currentHash] = currentHashCount;

            emit HashStored(currentHashCount, hashes[i], "Hash stored successfully.");

            // Store the result for that hash
            results[i] = StoreResult({
                Hash: hashes[i],
                stored: true
            });

            // Increment the hash count being stored
            currentHashCount++;
        }

        return results; // Return the result for each hash processed
    }

    // Struct for verification results
    struct VerificationResult {
        string verifiedHashes;
        bool verificationResults;  // Only need the verification result
    }

    // Function to verify hashes in bulk
    function verifyHashesByValue(string[] memory hashesToVerify) public view returns (VerificationResult[] memory) {
        uint256 length = hashesToVerify.length;
        VerificationResult[] memory results = new VerificationResult[](length);

        for (uint256 i = 0; i < length; i++) {
            string memory hashToVerify = hashesToVerify[i]; // to store current being handled from array
            bool isVerified = false;

            // Check if the hash is stored
            if (storedHash[hashToVerify]) {
                isVerified = true;
            }

            results[i] = VerificationResult({
                verifiedHashes: hashToVerify,
                verificationResults: isVerified
            });
        }

        return results;
    }

    // Function to get the stored hashes (for debugging or access purposes)
    function getStoredHashes() public view returns (string[] memory) {
        return hashesArray;
    }
}