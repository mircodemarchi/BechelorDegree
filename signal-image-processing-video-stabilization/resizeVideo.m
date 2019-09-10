function sub = resizeVideo(frames, resize_factor)
%RESIZEVIDEO Scalo le dimensioni delle immagini del video di un fattore.
%  INPUT
%   frames: Il mio video di frames sul quale voglio applicare il resize;
%   resize_factor: Fattore di scalatura:
%                     1    -> Nessuna scalatura
%                     >1   -> Immagini più grandi
%                     <1   -> Immagini più piccole
%  OUTPUT
%   sub: Video di frames risultato dalla scalatura.
%

% Check su resize_factor > 0
if resize_factor <= 0
    return
end

% Scorro tutto il video di frame e applico la riscalatura.
for i=1:size(frames,4)
    sub(:,:,:,i) = imresize(frames(:,:,:,i), resize_factor);
    i
end

end

