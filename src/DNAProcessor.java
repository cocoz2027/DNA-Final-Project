import java.io.*;
import java.util.Scanner;

public class DNAProcessor {

    private static final String FILE_NAME = "dna_data.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        // Display the welcome page
        System.out.println("Welcome to the DNA Processor Program!");

        while (!quit) {
            // Display the main menu
            System.out.println("\nChoose an option:");
            System.out.println("1. DNA Translation");
            System.out.println("2. DNA Analysis");
            System.out.println("3. View File");
            System.out.println("4. Quit");
            int choice = scanner.nextInt();

            if (choice == 1) {
                // DNA Translation
                System.out.println("\nEnter a DNA sequence:");
                String dnaSequence = scanner.next();

                if (isValidDNA(dnaSequence)) {
                    // Transcribe DNA to RNA
                    String rnaSequence = transcribeDNAtoRNA(dnaSequence);
                    System.out.println("RNA Sequence: " + rnaSequence);

                    // Translate RNA to Protein
                    String proteinSequence = translateRNAtoProtein(rnaSequence);
                    if (proteinSequence.equals("Invalid")) {
                        System.out.println("Invalid RNA sequence for translation.");
                    } else {
                        System.out.println("Protein Sequence: " + proteinSequence);

                        // Save translation to the file
                        saveToFile("DNA: " + dnaSequence + ", RNA: " + rnaSequence + ", Protein: " + proteinSequence);
                    }
                } else {
                    System.out.println("Invalid DNA sequence. Please enter a valid DNA sequence (A, T, C, G).");
                }
            } else if (choice == 2) {
                // DNA Analysis
                System.out.println("\nChoose an analysis option:");
                System.out.println("1. Ancestry");
                System.out.println("2. Height");
                System.out.println("3. Alzheimer's Disease Risk");
                int analysisChoice = scanner.nextInt();

                if (analysisChoice == 1) {
                    analyzeAncestry(scanner);
                } else if (analysisChoice == 2) {
                    analyzeHeight(scanner);
                } else if (analysisChoice == 3) {
                    analyzeAlzheimers(scanner);
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } else if (choice == 3) {
                // View File
                System.out.println("\nFile contents:");
                readFromFile();
            } else if (choice == 4) {
                // Quit
                quit = true;
                System.out.println("Exiting the program. Goodbye!");
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static boolean isValidDNA(String dna) {
        return dna.matches("[ATCG]+");
    }

    private static String transcribeDNAtoRNA(String dna) {
        // Transcription logic:
        // C → G, A → U, T → A, G → C
        StringBuilder rna = new StringBuilder();
        for (char nucleotide : dna.toCharArray()) {
            switch (nucleotide) {
                case 'C':
                    rna.append('G');
                    break;
                case 'A':
                    rna.append('U');
                    break;
                case 'T':
                    rna.append('A');
                    break;
                case 'G':
                    rna.append('C');
                    break;
                default:
                    // Shouldn't happen, as input is validated
                    throw new IllegalArgumentException("Invalid nucleotide in DNA sequence.");
            }
        }
        return rna.toString();
    }

    private static String translateRNAtoProtein(String rna) {
        String[] codons = rna.split("(?<=\\G...)"); // Split RNA into codons (groups of 3)

        // Find the first start codon (AUG)
        int startCodonIndex = -1;
        for (int i = 0; i < codons.length; i++) {
            if (codons[i].equals("AUG")) {
                startCodonIndex = i;
                break;
            }
        }
        if (startCodonIndex == -1) return "Invalid"; // No start codon found

        // Find the first stop codon (UAA, UAG, UGA)
        int stopCodonIndex = -1;
        for (int i = 0; i < codons.length; i++) {
            if (codons[i].equals("UAA") || codons[i].equals("UAG") || codons[i].equals("UGA")) {
                stopCodonIndex = i;
                break;
            }
        }
        if (stopCodonIndex == -1) return "Invalid"; // No stop codon found
        if (startCodonIndex > stopCodonIndex) return "Invalid"; // Start codon must appear before stop codon

        // Translate codons between start and stop codons
        StringBuilder protein = new StringBuilder();
        for (int i = startCodonIndex; i < stopCodonIndex; i++) {
            protein.append(translateCodon(codons[i]));
        }
        return protein.toString();
    }

    private static String translateCodon(String codon) {
        switch (codon) {
            case "UUU":
            case "UUC":
                return "F"; // Phenylalanine
            case "UUA":
            case "UUG":
            case "CUU":
            case "CUC":
            case "CUA":
            case "CUG":
                return "L"; // Leucine
            case "AUU":
            case "AUC":
            case "AUA":
                return "I"; // Isoleucine
            case "AUG":
                return "M"; // Methionine (Start Codon)
            case "GUU":
            case "GUC":
            case "GUA":
            case "GUG":
                return "V"; // Valine
            case "UCU":
            case "UCC":
            case "UCA":
            case "UCG":
            case "AGU":
            case "AGC":
                return "S"; // Serine
            case "CCU":
            case "CCC":
            case "CCA":
            case "CCG":
                return "P"; // Proline
            case "ACU":
            case "ACC":
            case "ACA":
            case "ACG":
                return "T"; // Threonine
            case "GCU":
            case "GCC":
            case "GCA":
            case "GCG":
                return "A"; // Alanine
            case "UAU":
            case "UAC":
                return "Y"; // Tyrosine
            case "CAU":
            case "CAC":
                return "H"; // Histidine
            case "CAA":
            case "CAG":
                return "Q"; // Glutamine
            case "AAU":
            case "AAC":
                return "N"; // Asparagine
            case "AAA":
            case "AAG":
                return "K"; // Lysine
            case "GAU":
            case "GAC":
                return "D"; // Aspartic acid
            case "GAA":
            case "GAG":
                return "E"; // Glutamic acid
            case "UGU":
            case "UGC":
                return "C"; // Cysteine
            case "UGG":
                return "W"; // Tryptophan
            case "CGU":
            case "CGC":
            case "CGA":
            case "CGG":
            case "AGA":
            case "AGG":
                return "R"; // Arginine
            case "GGU":
            case "GGC":
            case "GGA":
            case "GGG":
                return "G"; // Glycine
            default:
                return ""; // Invalid or unknown codon
        }
    }

    private static void analyzeAncestry(Scanner scanner) {
        System.out.println("Enter the genotype for rsid: rs1426654:");
        String genotype = scanner.next();
        String result;

        switch (genotype) {
            case "AA":
                result = "European ancestry. Probably light-skinned.";
                break;
            case "AG":
                result = "Probably mixed African/European ancestry.";
                break;
            case "GG":
                result = "Probably Asian or African ancestry with darker-skinned.";
                break;
            default:
                result = "Invalid input or no DNA info on skin type.";
        }

        System.out.println(result);
        saveToFile("Ancestry Analysis: " + result);
    }

    private static void analyzeHeight(Scanner scanner) {
        System.out.println("Enter the genotype for rsid: rs12740374:");
        String genotype = scanner.next();
        String result;

        switch (genotype) {
            case "AA":
                result = "Shorter stature.";
                break;
            case "AG":
                result = "Intermediate height.";
                break;
            case "GG":
                result = "Taller stature.";
                break;
            default:
                result = "Invalid input or no DNA info on height.";
        }

        System.out.println(result);
        saveToFile("Height Analysis: " + result);
    }

    private static void analyzeAlzheimers(Scanner scanner) {
        System.out.println("Enter the genotype for rsid: rs429358:");
        String genotype = scanner.next();
        String result;

        switch (genotype) {
            case "CC":
                result = "Lower risk; no APOE ε4 allele.";
                break;
            case "CT":
                result = "Intermediate risk; one APOE ε4 allele.";
                break;
            case "TT":
                result = "Higher risk; two APOE ε4 alleles.";
                break;
            default:
                result = "Invalid input or no DNA info on Alzheimer's disease risk.";
        }

        System.out.println(result);
        saveToFile("Alzheimer's Analysis: " + result);
    }

    private static void saveToFile(String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(data);
            writer.newLine();
            System.out.println("Data saved to file.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void readFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found. No data to display.");
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }
}