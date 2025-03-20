"use client";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import {
  Dropzone,
  DropZoneArea,
  DropzoneMessage,
  DropzoneTrigger,
  useDropzone,
} from "@/components/ui/dropzone";
import { useUserStore } from "@/stores/user-store";
import axios from "axios";
import { useCallback, useEffect, useState } from "react";
import { RefreshCw } from "lucide-react";
import { UUID } from "crypto";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";

interface ReceiptResponse {
  receiptId: UUID;
  image: string; // Base64 encoded image
  amount: number | null;
}

export default function Receipt() {
  const { id: userId, bearerToken } = useUserStore();
  const [isUploading, setIsUploading] = useState(false);
  const [receipts, setReceipts] = useState<ReceiptResponse[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [selectedReceipt, setSelectedReceipt] =
    useState<ReceiptResponse | null>(null);

  const [isEditing, setIsEditing] = useState(false);
  const [editedAmount, setEditedAmount] = useState<string>("");
  const dropzone = useDropzone({
    onDropFile: async (file: File) => {
      try {
        setIsUploading(true);

        const formData = new FormData();
        formData.append("file", file);

        const response = await axios.post(
          `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/receipt`,
          formData,
          {
            headers: {
              Authorization: `Bearer ${bearerToken}`,
              "Content-Type": "multipart/form-data",
            },
          }
        );

        return {
          status: "success",
          result: URL.createObjectURL(file),
          serverData: response.data,
        };
      } catch (error) {
        console.error("Upload error:", error);
        return {
          status: "error",
          error: axios.isAxiosError(error)
            ? error.response?.data?.message || error.message
            : "Failed to upload receipt",
        };
      } finally {
        setIsUploading(false);
      }
    },
    validation: {
      accept: {
        "image/*": [".png", ".jpg", ".jpeg"],
      },
      maxSize: 10 * 1024 * 1024,
      maxFiles: 1,
    },
    shiftOnMaxFiles: true,
  });
  const fetchReceipts = useCallback(async () => {
    if (!userId) return;

    try {
      setIsLoading(true);
      const response = await axios.get<ReceiptResponse[]>(
        `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/receipt`,
        {
          headers: {
            Authorization: `Bearer ${bearerToken}`,
          },
        }
      );
      setReceipts(response.data);
      console.log("Fetched receipts:", response.data);
    } catch (error) {
      console.error("Error fetching receipts:", error);
    } finally {
      setIsLoading(false);
    }
  }, [userId, bearerToken]);

  useEffect(() => {
    fetchReceipts();

    const intervalId = setInterval(() => {
      fetchReceipts();
    }, 60000);

    return () => clearInterval(intervalId);
  }, [fetchReceipts]);

  useEffect(() => {
    if (isUploading === false) {
      fetchReceipts();
    }
  }, [isUploading, fetchReceipts]);

  const updateReceiptAmount = async () => {
    if (!selectedReceipt) return;

    try {
      const amount = parseFloat(editedAmount);
      if (isNaN(amount)) return;

      const receiptId = selectedReceipt?.receiptId ?? null;

      await axios.patch(
        `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/receipt`,
        {
          receiptId: receiptId,
          amount: amount,
        },
        {
          headers: {
            Authorization: `Bearer ${bearerToken}`,
          },
        }
      );

      setReceipts(
        receipts.map((receipt) =>
          receipt.receiptId === selectedReceipt.receiptId
            ? { ...receipt, amount }
            : receipt
        )
      );

      setSelectedReceipt({ ...selectedReceipt, amount });
      setIsEditing(false);
    } catch (error) {
      console.error("Error updating receipt:", error);
    }
  };

  useEffect(() => {
    if (!selectedReceipt) {
      setIsEditing(false);
    }
  }, [selectedReceipt]);

  return (
    <div className="m-3">
      <div className="flex justify-between items-center mb-4">
        <div>
          <h2 className="text-2xl font-bold tracking-tight">Receipts</h2>
          <p className="text-muted-foreground">Snap and track receipts</p>
        </div>
        <Button
          variant="outline"
          size="sm"
          onClick={() => fetchReceipts()}
          disabled={isLoading}
        >
          {isLoading ? (
            <>
              <RefreshCw className="mr-2 h-4 w-4 animate-spin" />
              Refreshing...
            </>
          ) : (
            <>
              <RefreshCw className="mr-2 h-4 w-4" />
              Refresh
            </>
          )}
        </Button>
      </div>
      <Dropzone {...dropzone}>
        <div className="flex justify-between">
          <DropzoneMessage />
        </div>
        <DropZoneArea>
          <DropzoneTrigger className="flex gap-8 bg-transparent text-sm">
            <div className="flex flex-col gap-1 font-semibold">
              <p>Upload Receipt</p>
            </div>
          </DropzoneTrigger>
        </DropZoneArea>
      </Dropzone>
      <div className="grid grid-cols-3 gap-4 p-4">
        {receipts.map((receipt, index) => (
          <Card key={index} className="w-64">
            <CardContent className="p-4 flex flex-col items-center">
              <img
                src={`data:image/png;base64,${receipt.image}`}
                alt={`Receipt ${index}`}
                className="w-full h-40 object-cover rounded-lg cursor-pointer"
                onClick={() => setSelectedReceipt(receipt)}
              />
              <p className="text-center font-semibold mt-2">
                {receipt.amount !== null
                  ? `$${receipt.amount.toFixed(2)}`
                  : "Unknown"}
              </p>
            </CardContent>
          </Card>
        ))}
      </div>
      <Dialog
        open={!!selectedReceipt}
        onOpenChange={(open) => !open && setSelectedReceipt(null)}
      >
        <DialogContent className="sm:max-w-4xl max-h-[90vh]">
          <DialogHeader>
            <DialogTitle>Receipt Details</DialogTitle>
          </DialogHeader>
          {selectedReceipt && (
            <>
              <img
                src={`data:image/png;base64,${selectedReceipt.image}`}
                alt="Receipt"
                className="max-h-[70vh] w-full object-contain"
              />
              <div className="flex items-center justify-center gap-2 mt-2">
                {isEditing ? (
                  <>
                    <Input
                      type="number"
                      step="0.01"
                      value={editedAmount}
                      onChange={(e) => setEditedAmount(e.target.value)}
                      className="w-24 text-center"
                      placeholder="0.00"
                    />
                    <Button size="sm" onClick={updateReceiptAmount}>
                      Save
                    </Button>
                    <Button
                      size="sm"
                      variant="outline"
                      onClick={() => setIsEditing(false)}
                    >
                      Cancel
                    </Button>
                  </>
                ) : (
                  <>
                    <p className="text-center font-semibold">
                      {selectedReceipt.amount !== null
                        ? `$${selectedReceipt.amount.toFixed(2)}`
                        : "Unknown"}
                    </p>
                    <Button
                      size="sm"
                      variant="outline"
                      onClick={() => {
                        setEditedAmount(
                          selectedReceipt.amount !== null
                            ? selectedReceipt.amount.toString()
                            : ""
                        );
                        setIsEditing(true);
                      }}
                    >
                      Edit
                    </Button>
                  </>
                )}
              </div>
            </>
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
}
