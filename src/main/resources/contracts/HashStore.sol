
pragma solidity ^0.8.0;

contract HashStore {

    mapping(string => bool) private storedHash;
    mapping(string => uint256) private hashToId;
    uint256 private currentHashCount = 1;

    event HashStored(uint256 indexed id, string hash, string message);

    // Struct for storing hash storage results
    struct StoreResult {
        string Hash;
        bool stored;
    }

// Function to store hashes by taking input hashes as array and returns array of objects
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
        bool verificationResults;
    }

    // Function to verify hashes in bulk
    function verifyHashesByValue(string[] memory hashesToVerify) public view returns (VerificationResult[] memory) {
        uint256 length = hashesToVerify.length;
        VerificationResult[] memory results = new VerificationResult[](length);

        for (uint256 i = 0; i < length; i++) {
            string memory hashToVerify = hashesToVerify[i]; // to store current being handled from array
            bool isVerified = false;

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
}